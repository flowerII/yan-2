package cn.qianshu.yan.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import cn.qianshu.yan.entity.Role;
import cn.qianshu.yan.entity.User;
import cn.qianshu.yan.repository.RoleRepository;
import cn.qianshu.yan.repository.UserRepository;
import cn.qianshu.yan.service.ImageCode;
import cn.qianshu.yan.service.MailService;

@Controller
@Transactional
public class UserController {
	
	private static Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
    private MailService mailService;
	
	@Autowired
    private TemplateEngine templateEngine;
	
	@Autowired
	private RoleRepository roleRepository;
	
	//注册页面获取
	@RequestMapping(value = "/registry", method=RequestMethod.GET)
	public String registryGet() {
		return "registry";
	}
	
	//提交注册数据
	@RequestMapping(value = "/registry", method=RequestMethod.POST)
	@ResponseBody
	public String registryPost(User user,HttpServletRequest request, HttpSession session) throws AddressException {
		String checkCode = request.getParameter("checkCode");
        Object cko = session.getAttribute("simpleCaptcha") ; //验证码对象
        if(cko == null){
            request.setAttribute("errorMsg", "验证码已失效，请重新输入！");
            return "验证码已失效，请重新输入！";
        }

        String captcha = cko.toString();
        Date now = new Date();
        Long codeTime = Long.valueOf(session.getAttribute("codeTime")+"");
        if(StringUtils.isEmpty(checkCode) || captcha == null ||  !(checkCode.equalsIgnoreCase(captcha))){
            request.setAttribute("errorMsg", "验证码错误！");
            return "验证码错误！";
        }else if ((now.getTime()-codeTime)/1000/60>5){//验证码有效时长为5分钟
            request.setAttribute("errorMsg", "验证码已失效，请重新输入！");
            return "验证码已失效，请重新输入！";
        }else {
            session.removeAttribute("simpleCaptcha");
            Role role=this.roleRepository.findByName("user");
            List<Role> listRole =new ArrayList<Role>();
            listRole.add(role);
            user.setRoles(listRole);
            user.setActive(false);
            String activeCode=UUID.randomUUID().toString();
            request.getSession().setAttribute("activeCode",activeCode);
            
            System.out.println(activeCode);
            user.setActiveCode(activeCode);
            this.userRepository.save(user);
            logger.info("注册user:" + user.getUsername());
            
            //创建邮件正文
            Context context = new Context();
            context.setVariable("activeCode", activeCode);
            String emailContent = templateEngine.process("emailactivitycode", context);

            mailService.sendHtmlMail(user.getEmail(),"主题：卓越书城注册激活邮件",emailContent);
            //mailService.sendHtmlMail("1479676948@qq.com","主题：这是模板邮件",content);
            
            return "1";
        }
	}
    
	//验证账号是否存在
    @RequestMapping(value = "/checkUserName", method=RequestMethod.POST)
    @ResponseBody
    public String checkUserName(@RequestParam("username") String username) {
    	 User u=this.userRepository.findByUsername(username);
    	 if(u!=null) {
    		 return "0";
    	 }
        return "1";
    }
    //激活用户
    @RequestMapping(value = "/activeUser", method=RequestMethod.GET)
    @ResponseBody
    public String activeUser(@RequestParam("activeCode") String activeCode,HttpSession session) {

        String activeCode1 = (String) session.getAttribute("activeCode");
        if(!activeCode.equals(activeCode1)) {
        	return "激活码不正确！";
        }else {
        	User u=this.userRepository.findByActiveCode(activeCode);
        	u.setActiveCode("");
        	u.setActive(true);
        	this.userRepository.save(u);
        }
        
        return "1";
    }
    
    //验证码
    @RequestMapping(value = "/images/imagecode")
    public String imagecode(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        OutputStream os = response.getOutputStream();
        Map<String,Object> map = ImageCode.getImageCode(200, 20, os);

        String simpleCaptcha = "simpleCaptcha";
        request.getSession().setAttribute(simpleCaptcha, map.get("strEnsure").toString().toLowerCase());
        request.getSession().setAttribute("codeTime",new Date().getTime());

        try {
            ImageIO.write((BufferedImage) map.get("image"), "JPEG", os);
        } catch (IOException e) {
            return "";
        }
        return null;
    }
    
    //忘记密码，发送邮箱验证码
    @RequestMapping(value = "/email_code", method=RequestMethod.POST)
    @ResponseBody
    public String email_code(HttpServletRequest request,@RequestParam("email") String email) {
    	//创建邮件正文
        Context context = new Context();
        int emailcode = (int)((Math.random()*9+1)*1000);
       
        request.getSession().setAttribute("emailcodeTime",new Date().getTime());
        request.getSession().setAttribute("emailcode",emailcode);
        
        context.setVariable("emailcode", emailcode);
        String emailContent = templateEngine.process("emailpasswordcode", context);

        mailService.sendHtmlMail(email,"主题：卓越书城账号密码重置邮件",emailContent);
        return "1";
    }

    
    //密码重置页面获取
  	@RequestMapping(value = "/repassword", method=RequestMethod.GET)
  	public String repasswordGet() {
  		return "repassword";
  	}
  //密码重置页面提交
  	@RequestMapping(value = "/repassword", method=RequestMethod.POST)
  	@ResponseBody
  	public String repasswordPost(HttpServletRequest request,@RequestParam("email") String email,HttpSession session,@RequestParam("code") String code,@RequestParam("password") String password) {
  		
  		System.out.println(password);
  		System.out.println(code);
  		System.out.println(email);
  		
  		String emailcode = session.getAttribute("emailcode").toString();
  		System.out.println(emailcode);
  		
  		Date now = new Date();
        Long emailcodeTime = Long.valueOf(session.getAttribute("emailcodeTime")+"");
        if ((now.getTime()-emailcodeTime)/1000/60>5){//验证码有效时长为5分钟
            request.setAttribute("errorMsg", "验证码已失效，请重新获取！");
            return "验证码已失效，请重新获取！";
        }else if(!emailcode.equals(code)){
        	return "验证码错误！";
        }
        else {
        	User u=this.userRepository.findByEmail(email);
        	u.setPassword(password);
        	this.userRepository.save(u);
        	return "1";
        }
  	}
}
package cn.qianshu.yan.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.qianshu.yan.entity.Book;
import cn.qianshu.yan.entity.Category;
import cn.qianshu.yan.entity.User;
import cn.qianshu.yan.repository.BookRepository;
import cn.qianshu.yan.repository.CategoryRepository;
import cn.qianshu.yan.repository.UserRepository;
import cn.qianshu.yan.service.CategoryService;
import cn.qianshu.yan.util.FileUtil;
import cn.qianshu.yan.util.PicUtils;

@Controller
@Transactional
@RequestMapping("/book")
public class BookController {
	
	private static Logger logger = LoggerFactory.getLogger(BookController.class);
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private CategoryService categoryService;
    
	//添加图书页面获取
	@RequestMapping(value="/add", method = RequestMethod.GET)
    public String goUploadImg(Model model) {  
		//获得所有分类
    	List<Category> categoryList=categoryRepository.findAll();
    	model.addAttribute("categoryList", categoryList);
        return "add_book";
    }
	
	//添加图书表单提交
	@RequestMapping(value="/add", method = RequestMethod.POST)
    public @ResponseBody String uploadImg(@RequestParam("file") MultipartFile file,Book book,@RequestParam("c_id") int c_id,@RequestParam("isHot") boolean isHot,
            HttpServletRequest request) { 
        String fileName = file.getOriginalFilename();
        String filePath = request.getSession().getServletContext().getRealPath("imgupload/");
        try {
            FileUtil.uploadFile(file.getBytes(), filePath, fileName);
            PicUtils putils = new PicUtils(filePath+fileName);
			putils.resize(100, 100);
			Category c=categoryRepository.findOne(c_id);
			book.setCategory(c);
			book.setHot(isHot);
        	
        	int index = putils.getDestFile().lastIndexOf("\\imgupload");
    		String url = putils.getDestFile().substring(index,putils.getDestFile().toString().length());
    		
        	book.setPic(url);
        	bookRepository.save(book);
        } catch (Exception e) {
            // TODO: handle exception
        }
        //返回json
        return "1";
    }
	
	//根据id查找书籍
	@RequestMapping(value = "/find_by_id", method=RequestMethod.GET)
    public String find_book_by_id(@RequestParam("id") Integer id,Model model,@RequestParam("username") String username) {
		
		//获得用户信息
    	User user=new User();
    	if(!username.equals("0")) {
    		user=this.userRepository.findByUsername(username);
    	}else {
    		user.setUsername("0");
    	}   	    	
    	model.addAttribute("user", user);
    	
    	//导航栏
    	Page<Category> pageCategory=categoryService.pageCategory();
        model.addAttribute("pageCategory", pageCategory);
        
        //书籍
        Book book=bookRepository.findOne(id);
        model.addAttribute("book", book);

        return "book";
    }
    
}
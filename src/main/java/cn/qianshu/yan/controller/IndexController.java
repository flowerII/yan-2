package cn.qianshu.yan.controller;

import java.security.Principal;
import java.util.List;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.qianshu.yan.entity.Book;
import cn.qianshu.yan.entity.Category;
import cn.qianshu.yan.entity.User;
import cn.qianshu.yan.repository.BookRepository;
import cn.qianshu.yan.repository.UserRepository;
import cn.qianshu.yan.service.CategoryService;

@Controller
@Transactional
public class IndexController {
	
	private static Logger logger = LoggerFactory.getLogger(IndexController.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private CategoryService categoryService;
	
	//未登录首页
    @GetMapping(value = "/")
    public String index(Model model){
    	//导航栏
    	Page<Category> pageCategory=categoryService.pageCategory();
        model.addAttribute("pageCategory", pageCategory);
        
        //最新商品
        Sort sort = new Sort(Direction.DESC, "id");
        Pageable p1 = new PageRequest(0, 8, sort);
        Page<Book> bookNewList=bookRepository.findByIdAndNotNull(p1);
        model.addAttribute("bookNewList", bookNewList);
        
        //热门商品
        Pageable p3 = new PageRequest(0, 8, sort);
        List<Book> bookIsHotList=bookRepository.findByIsHotLimit(p3);
        model.addAttribute("bookIsHotList", bookIsHotList);
        
        //用户设置
        User user=new User();
        user.setUsername("0");
        model.addAttribute("user", user);
        return "index";
    }
    
    //登录后首页
    @GetMapping(value = "/loginindex")
    public String loginindex(Model model,Principal user1){
        
    	//导航栏
        Page<Category> pageCategory=categoryService.pageCategory();
        model.addAttribute("pageCategory", pageCategory);
        
        //最新商品
        Sort sort = new Sort(Direction.DESC, "id");
        Pageable p1 = new PageRequest(0, 8, sort);
        Page<Book> bookNewList=bookRepository.findByIdAndNotNull(p1);
        model.addAttribute("bookNewList", bookNewList);
        
        //热门商品
        Pageable p3 = new PageRequest(0, 8, sort);
        List<Book> bookIsHotList=bookRepository.findByIsHotLimit(p3);
        model.addAttribute("bookIsHotList", bookIsHotList);
        
        //用户
        User user=this.userRepository.findByUsername(user1.getName());       
        model.addAttribute("user", user);
        
        return "index";
    }
    
    //搜索
    @RequestMapping(value = "/search", method=RequestMethod.POST)
    public String getSearch(@RequestParam("scontent") String scontent,@RequestParam(value = "page", defaultValue = "0") Integer page,Model model,@RequestParam("username") String username) {
    	
    	//用户
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
        
        //搜索内容
        Sort sort = new Sort(Direction.DESC, "id");
    	Pageable pageable1 = new PageRequest(page, 6, sort);
        Page<Book> searchBookList=bookRepository.findByString(scontent, pageable1);
        model.addAttribute("searchBookList", searchBookList);
        model.addAttribute("scontent", scontent);
        
        return "search";
    }
    
    //搜索
    @RequestMapping(value = "/search", method=RequestMethod.GET)
    public String getSearchGet(@RequestParam("scontent") String scontent,@RequestParam(value = "page", defaultValue = "0") Integer page,Model model,@RequestParam("username") String username) {
    	
    	//用户
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
        
        //搜索内容
        Sort sort = new Sort(Direction.DESC, "id");
    	Pageable pageable1 = new PageRequest(page, 6, sort);
        Page<Book> searchBookList=bookRepository.findByString(scontent, pageable1);
        model.addAttribute("searchBookList", searchBookList);
        model.addAttribute("scontent", scontent);
        
        return "search";
    }
}
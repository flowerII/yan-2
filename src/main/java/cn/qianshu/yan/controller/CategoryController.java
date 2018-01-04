package cn.qianshu.yan.controller;

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

import cn.qianshu.yan.entity.Book;
import cn.qianshu.yan.entity.Category;
import cn.qianshu.yan.entity.User;
import cn.qianshu.yan.repository.BookRepository;
import cn.qianshu.yan.repository.CategoryRepository;
import cn.qianshu.yan.repository.UserRepository;
import cn.qianshu.yan.service.CategoryService;

@Controller
@Transactional
public class CategoryController {
	
	private static Logger logger = LoggerFactory.getLogger(CategoryController.class);
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private CategoryService categoryService;
	
    
    @RequestMapping(value = "/category", method=RequestMethod.GET)
    public String getPageByCategory(@RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam("id") Integer id,Model model,@RequestParam("username") String username) {
    	
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
        
        //分类
        Category category1=this.categoryRepository.findOne(id);
        model.addAttribute("category1", category1);

        //分页获取
        Sort sort = new Sort(Direction.DESC, "id");
        Pageable pageable = new PageRequest(page, 16, sort);       
        Page<Book> pageBook=bookRepository.findByCategoryAndPage(category1, pageable);
        model.addAttribute("pageBook", pageBook);
        
        return "categorypage";
    }
}
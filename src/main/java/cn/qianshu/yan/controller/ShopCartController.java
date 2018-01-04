package cn.qianshu.yan.controller;

import java.security.Principal;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.qianshu.yan.entity.Book;
import cn.qianshu.yan.entity.CartItem;
import cn.qianshu.yan.entity.Category;
import cn.qianshu.yan.entity.ShopCart;
import cn.qianshu.yan.entity.User;
import cn.qianshu.yan.repository.BookRepository;
import cn.qianshu.yan.repository.CartItemRepository;
import cn.qianshu.yan.repository.ShopCartRepository;
import cn.qianshu.yan.repository.UserRepository;
import cn.qianshu.yan.service.CategoryService;

@Controller
@Transactional
@RequestMapping("/cart")
public class ShopCartController {
	
	private static Logger logger = LoggerFactory.getLogger(ShopCartController.class);
	
	@Autowired
	private ShopCartRepository shopCartRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartItemRepository cartItemRepository;
		
	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private CategoryService categoryService;
	    
    @RequestMapping(value = "/add", method=RequestMethod.POST)
    public String add_car(@RequestParam("id") Integer id,@RequestParam("num") int num,Principal user) {
    	Book book=bookRepository.findOne(id);
    	User u=this.userRepository.findByUsername(user.getName());
    	
    	CartItem item=this.cartItemRepository.findByUserAndBook(u,book);
    	if(item!=null) {
    		item.setCount(item.getCount()+num);
    		item.setSubtotal(item.getCount()*book.getPrice());
    	}else {
    		item=new CartItem();
    		item.setBook(book);
        	item.setCount(num);
        	item.setSubtotal(num*book.getPrice());
        	item.setUser(u);
    	}
    	this.cartItemRepository.save(item);
    	
    	//判断购物车是否存在，不存在则创建
    	ShopCart cart=this.shopCartRepository.finByUserName(user.getName());
    	if(cart==null) {
    		cart=new ShopCart();
    		cart.setUser(u);
    		cart.setTotal(0);
    		cart.addCart(item);
    		this.shopCartRepository.save(cart);
    	}else {
    		cart.addCart(item);
    		this.shopCartRepository.save(cart);
    	}
    	logger.info("添加购物");
        return "redirect:/cart/get";
    }
    
    @RequestMapping(value = "/remove", method=RequestMethod.POST)
    public String car_remove(@RequestParam("id") Integer id,Principal user) {
    	
    	ShopCart cart=this.shopCartRepository.finByUserName(user.getName());
    	cart.removeCart(id);
    	
    	Book book=bookRepository.findOne(id);
    	User u=this.userRepository.findByUsername(user.getName());   	
    	CartItem cartItem=this.cartItemRepository.findByUserAndBook(u, book);

    	this.shopCartRepository.save(cart);
    	this.cartItemRepository.delete(cartItem);
    	
        return "redirect:/cart/get";
    }
    
    @RequestMapping(value = "/clear", method=RequestMethod.POST)
    public String clear_cart(Principal user) {
    	
    	ShopCart cart=this.shopCartRepository.finByUserName(user.getName());
    	User u=this.userRepository.findByUsername(user.getName());
    	
    	cart.clearCart();
    	this.shopCartRepository.save(cart);
    	
    	this.cartItemRepository.deleteByUser(u);
    	
        return "redirect:/cart/get";
    }
    
    @RequestMapping(value = "/get", method=RequestMethod.GET)
    public String get_car(Principal user,Model model) {   	

    	//导航栏
    	Page<Category> pageCategory=categoryService.pageCategory();
        model.addAttribute("pageCategory", pageCategory);
        
    	User u=this.userRepository.findByUsername(user.getName());
    	ShopCart cart=this.shopCartRepository.finByUserName(u.getUsername());
    	if(cart==null) {
    		cart=new ShopCart();
    		cart.setUser(u);
    		cart.setTotal(0);
    		this.shopCartRepository.save(cart);
    	}
    	
    	Collection<CartItem> cartItems=cart.getCartItems();
    	boolean hasItems=false;
    	if(!cartItems.isEmpty()) {
    		hasItems=true;
    	}
    	model.addAttribute("hasItems", hasItems);
    	model.addAttribute("cartItems", cartItems);
    	model.addAttribute("user", u);
        return "cart";
    }
    
}
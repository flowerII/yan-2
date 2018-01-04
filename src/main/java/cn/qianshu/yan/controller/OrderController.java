package cn.qianshu.yan.controller;

import java.security.Principal;
import java.util.Date;

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
import cn.qianshu.yan.entity.CartItem;
import cn.qianshu.yan.entity.Category;
import cn.qianshu.yan.entity.Order1;
import cn.qianshu.yan.entity.OrderItem;
import cn.qianshu.yan.entity.ShopCart;
import cn.qianshu.yan.entity.User;
import cn.qianshu.yan.repository.BookRepository;
import cn.qianshu.yan.repository.CartItemRepository;
import cn.qianshu.yan.repository.CategoryRepository;
import cn.qianshu.yan.repository.Order1Repository;
import cn.qianshu.yan.repository.OrderItemRepository;
import cn.qianshu.yan.repository.ShopCartRepository;
import cn.qianshu.yan.repository.UserRepository;
import cn.qianshu.yan.service.CategoryService;

@Controller
@Transactional
@RequestMapping("/order")
public class OrderController {
	
	private static Logger logger = LoggerFactory.getLogger(OrderController.class);
	
	@Autowired
	private ShopCartRepository shopCartRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	@Autowired
	private Order1Repository order1Repository;
	
	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private CartItemRepository cartItemRepository;
	
	@Autowired
	private CategoryService categoryService;
    
    @RequestMapping(value = "/add", method=RequestMethod.POST)
    public String add_order(Principal user,Model model) {
    	//用户
    	User u=this.userRepository.findByUsername(user.getName());
    	model.addAttribute("user", u);

    	//导航栏
    	Page<Category> pageCategory=categoryService.pageCategory();
        model.addAttribute("pageCategory", pageCategory);
    	
    	//生成订单
    	ShopCart cart=this.shopCartRepository.finByUserName(u.getUsername());
    	Order1 order1=new Order1();
    	order1.setDate(new Date());
    	order1.setState(0);
    	order1.setTotal(cart.getTotal());
    	order1.setUser(u);
    	for(CartItem cartItem:cart.getCartItems()) {
    		//获得商品信息
    		Book book=this.bookRepository.findByName(cartItem.getBook().getName());
    		
    		//添加订单项
    		OrderItem orderItem=new OrderItem();
    		orderItem.setBook(book);
    		orderItem.setCount(cartItem.getCount());
    		orderItem.setSubtotal(cartItem.getSubtotal());
    		//保存订单项
    		this.orderItemRepository.save(orderItem);
    		//添加到订单
    		order1.getOrderItems().add(orderItem);
    		//修改库存
    		book.setNum(book.getNum()-cartItem.getCount());
    		this.bookRepository.save(book);
    	}
    	this.order1Repository.save(order1);
    	model.addAttribute("order", order1);
    	//清空购物车
    	cart.clearCart();
    	this.shopCartRepository.save(cart);   	
    	this.cartItemRepository.deleteByUser(u);
    	
    	logger.info("增加订单");
        return "order";
    }
    
    @RequestMapping(value = "/list", method=RequestMethod.GET)
    public String list_order(@RequestParam(value = "page", defaultValue = "0") Integer page,Principal user,Model model) {
    	//用户
    	User u=this.userRepository.findByUsername(user.getName());
    	model.addAttribute("user", u);

    	//导航栏
    	Page<Category> pageCategory=categoryService.pageCategory();
        model.addAttribute("pageCategory", pageCategory);
    	
    	//订单查询
        Sort sort = new Sort(Direction.DESC, "id");
        Pageable p1 = new PageRequest(page, 6, sort);
        Page<Order1> orderPage=this.order1Repository.findByUserAndPage(u,p1);
        boolean hasOrder=false;
        
        if(orderPage.hasContent()) {
        	hasOrder=true;
        }
    	
        model.addAttribute("orderPage", orderPage);
        model.addAttribute("hasOrder", hasOrder);
    	
        return "order_list";
    }
    
}
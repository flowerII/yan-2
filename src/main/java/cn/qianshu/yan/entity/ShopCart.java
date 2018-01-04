package cn.qianshu.yan.entity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class ShopCart {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@OneToOne
    @JoinColumn(name = "id")
	private User user;
	
	@ElementCollection(fetch=FetchType.EAGER,
            targetClass=CartItem.class) //指定元素中集合的类型  
	private Map<Integer,CartItem> map=new HashMap<Integer, CartItem>();
	
	private float total;
	
	// Cart对象中有一个叫cartItems属性.
	public Collection<CartItem> getCartItems(){
		return map.values();
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Map<Integer, CartItem> getMap() {
		return map;
	}

	public void setMap(Map<Integer, CartItem> map) {
		this.map = map;
	}

	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}
	
	// 购物车的功能:
	// 1.将购物项添加到购物车
	public void addCart(CartItem cartItem) {
		// 判断购物车中是否已经存在该购物项:
		/*
		 *  * 如果不存在:
		 *  	* 向map中添加购物项
		 *  	* 总计 = 总计 + 购物项小计
		 */
		// 获得商品id.
		Integer pid = cartItem.getBook().getId();
		// 判断购物车中是否已经存在该购物项:
		if(map.containsKey(pid)){
			// 存在
			
		}else{
			// 不存在
			map.put(pid, cartItem);
		}
		// 设置总计的值
		total += cartItem.getSubtotal();
	}

	// 2.从购物车移除购物项
	public void removeCart(Integer pid) {
		// 将购物项移除购物车:
		CartItem cartItem = map.remove(pid);
		// 总计 = 总计 -移除的购物项小计:
		total -= cartItem.getSubtotal();
	}

	// 3.清空购物车
	public void clearCart() {
		// 将所有购物项清空
		map.clear();
		// 将总计设置为0
		total = 0;
	}
}

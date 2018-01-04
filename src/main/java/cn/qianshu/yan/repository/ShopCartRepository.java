package cn.qianshu.yan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import cn.qianshu.yan.entity.ShopCart;

@Repository
public interface ShopCartRepository extends JpaRepository<ShopCart, Integer> {
	
	@Query("select s from ShopCart s where s.user.username =?1")
	ShopCart finByUserName(String userName);

}

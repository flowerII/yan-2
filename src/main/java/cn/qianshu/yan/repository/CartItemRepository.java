package cn.qianshu.yan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cn.qianshu.yan.entity.Book;
import cn.qianshu.yan.entity.CartItem;
import cn.qianshu.yan.entity.User;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

	CartItem findByUserAndBook(User u, Book book);

	void deleteByUser(User u);

}

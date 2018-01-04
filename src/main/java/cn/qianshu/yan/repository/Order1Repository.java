package cn.qianshu.yan.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import cn.qianshu.yan.entity.Order1;
import cn.qianshu.yan.entity.User;

@Repository
public interface Order1Repository extends JpaRepository<Order1, Integer> {

	@Query("select o from Order1 o where o.user in (?1) order by o.id desc")
	Page<Order1> findByUserAndPage(User u, Pageable pageable);

}

package cn.qianshu.yan.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import cn.qianshu.yan.entity.Book;
import cn.qianshu.yan.entity.Category;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer>,JpaSpecificationExecutor<Book> {
	
	@Query("select b from Book b where b.isHot=true and b.num>0 order by b.id desc")
	List<Book> findByIsHotLimit(Pageable pageable);
	
	@Query("select b from Book b where b.category in (?1) and b.num>0 order by b.id desc")
	Page<Book> findByCategoryAndPage(Category category,Pageable pageable);
	
	@Query("select b from Book b where b.category.name like :name or b.name like :name or b.author like :name order by b.id desc")
	Page<Book> findByString(@Param("name") String name,Pageable pageable);

	Book findByName(String name);

	@Query("select b from Book b where b.num>0 order by b.id desc")
	Page<Book> findByIdAndNotNull(Pageable p1);

}

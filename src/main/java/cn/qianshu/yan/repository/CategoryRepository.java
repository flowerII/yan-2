package cn.qianshu.yan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cn.qianshu.yan.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

}

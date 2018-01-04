package cn.qianshu.yan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cn.qianshu.yan.entity.City;

@Repository
public interface CityRepository extends JpaRepository<City, Integer> {

}

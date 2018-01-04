package cn.qianshu.yan.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.qianshu.yan.entity.City;
import cn.qianshu.yan.repository.CityRepository;

@Service
@Transactional
public class CityService {
	
	@Autowired
	private CityRepository cityRepository;
	
	public List<City> listCity(){
		
		return this.cityRepository.findAll();
	}

}

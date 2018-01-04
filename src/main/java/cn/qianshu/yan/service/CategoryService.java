package cn.qianshu.yan.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.qianshu.yan.entity.Category;
import cn.qianshu.yan.repository.CategoryRepository;

@Service
@Transactional
public class CategoryService {
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	//导航栏分类随机分页获取
	public Page<Category> pageCategory(){
		
		int cNum=categoryRepository.findAll().size();
		int page=(int)cNum/6+1;
		Random rand = new Random();
		int startPage = rand.nextInt(page);
		Sort sort = new Sort(Direction.DESC, "id");
    	Pageable pageable = new PageRequest(startPage, 6, sort);
        return categoryRepository.findAll(pageable);
	}

}

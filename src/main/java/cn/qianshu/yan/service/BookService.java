package cn.qianshu.yan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.qianshu.yan.repository.BookRepository;

@Service
@Transactional
public class BookService {
	
	@Autowired
	private BookRepository bookRepository;

}

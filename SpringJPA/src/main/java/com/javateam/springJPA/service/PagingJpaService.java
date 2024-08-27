package com.javateam.springJPA.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.javateam.springJPA.domain.DemoVO;
import com.javateam.springJPA.repository.PagingJpaDAO;

@Service
@Transactional
public class PagingJpaService {
	
	@Autowired
	PagingJpaDAO dao;

    public List<DemoVO> findAll(String sort) {
    	
    	Direction direction = sort.equals("오름차순") ?  Direction.ASC : Direction.DESC;
    	
    	return (List<DemoVO>) dao.findAll(Sort.by(new Order(direction, "name")));
    }
    
    public Page<DemoVO> findAllByPaging(int page, int limit) {
    	
    	Pageable pageable = PageRequest.of(page-1, limit);

    	return dao.findAll(pageable);
    }
   
    public DemoVO findById(int id) {
    	
    	return dao.findById(id);
    }
    
}
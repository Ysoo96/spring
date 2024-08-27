package com.javateam.springJPA.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.javateam.springJPA.domain.DemoVO;

public interface PagingJpaDAO extends PagingAndSortingRepository<DemoVO, Integer> {
 
    Iterable<DemoVO> findAll(Sort sort);
   
    Page<DemoVO> findAll(Pageable pageable);
   
    DemoVO findById(int id);
   
} 
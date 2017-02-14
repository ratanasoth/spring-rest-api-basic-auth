package com.rupp.spring.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rupp.spring.dao.CategoryDaoImpl;
import com.rupp.spring.domain.DCategory;

@Service("categorySevice")
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryDaoImpl dao;
    
    @Override
    public List<DCategory> list() {
        return dao.list();
    }

    @Override
    public DCategory get(Long id) {
        return dao.get(id);
    }

    @Override
    public DCategory create(DCategory dCategory) {
        return dao.create(dCategory);
    }

    @Override
    public Long delete(Long id) {
        return dao.delete(id);
    }

    @Override
    public DCategory update(Long id, DCategory dCategory) {
        return dao.update(id, dCategory);
    }

  

}

package com.github.kickshare.service.impl;

import java.util.List;

import com.github.kickshare.db.jooq.tables.daos.CategoryDao;
import com.github.kickshare.domain.Category;
import com.github.kickshare.mapper.ExtendedMapper;
import com.github.kickshare.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Jan.Kucera
 * @since 24.4.2017
 */
@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private CategoryDao dao;
    private ExtendedMapper dozer;

    @Override
    public List<Category> getCategories() {
        return dozer.map(dao.findAll(), Category.class);
    }
}
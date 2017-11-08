package com.github.kickshare.service.impl;

import static com.github.kickshare.mapper.EntityMapper.category;

import java.util.List;

import com.github.kickshare.db.jooq.tables.daos.CategoryDaoDB;
import com.github.kickshare.domain.Category;
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
    private CategoryDaoDB dao;

    @Override
    public List<Category> getCategories() {
        return category().toDomain(dao.findAll());
    }
}

package com.github.kickshare.rest;

import java.util.List;

import com.github.kickshare.domain.Category;
import com.github.kickshare.service.CategoryService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jan.Kucera
 * @since 20.4.2017
 */
@RestController
@RequestMapping("/categories")
@AllArgsConstructor
public class CategoryEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryEndpoint.class);
    private CategoryService service;

    @GetMapping
    public List<Category> getCategories() {
        return service.getCategories();
    }

}

package com.github.kickshare.rest;

import java.util.Arrays;
import java.util.List;

import com.github.kickshare.domain.Category;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jan.Kucera
 * @since 20.4.2017
 */
@RestController
@RequestMapping("/categories/")
public class CategoryEndpoint {

    @GetMapping
    public List<Category> getCategories() {
        return Arrays.asList(new Category(1L, "Tabletop Games"), new Category(2L, "Video Games"));
    }
}

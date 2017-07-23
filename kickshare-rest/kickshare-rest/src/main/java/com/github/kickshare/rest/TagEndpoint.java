package com.github.kickshare.rest;

import java.util.List;

import com.github.kickshare.domain.Tag;
import com.github.kickshare.security.permission.Admin;
import com.github.kickshare.service.TagService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jan.Kucera
 * @since 17.7.2017
 */
@RestController
@AllArgsConstructor
@RequestMapping("/tags")
public class TagEndpoint {
    private TagService tagService;

    @GetMapping
    public List<Tag> getTags(@RequestParam("category_id") final Integer categoryId) {
        return tagService.getTags(categoryId);
    }

    @Admin
    @PostMapping
    public Tag createTag(@RequestBody final Tag tag, @RequestParam("category_id") final Integer categoryId) {
        return tagService.createTag(tag.getName(), categoryId);
    }
}

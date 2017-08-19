package com.github.kickshare.rest.group.domain;

import lombok.Data;

/**
 * @author Jan.Kucera
 * @since 5.4.2017
 */
@Data
public class SearchOptions {
    private String userId;
    private String categoryId;
    private com.github.kickshare.service.entity.SearchOptions options;
}

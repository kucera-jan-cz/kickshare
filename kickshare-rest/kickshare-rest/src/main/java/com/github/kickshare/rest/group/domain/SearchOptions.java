package com.github.kickshare.rest.group.domain;

import com.github.kickshare.service.GroupSearchOptions;
import lombok.Data;

/**
 * @author Jan.Kucera
 * @since 5.4.2017
 */
@Data
public class SearchOptions {
    private String userId;
    private String categoryId;
    private GroupSearchOptions options;
}

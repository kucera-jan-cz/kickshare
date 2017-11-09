package com.github.kickshare.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Jan.Kucera
 * @since 20.4.2017
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    private Long id;
    private String name;
    private Boolean isRoot;
    private Long parentId;
    private String slug;

}

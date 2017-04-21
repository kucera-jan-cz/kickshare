package com.github.kickshare.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Jan.Kucera
 * @since 20.4.2017
 */
@Data
@AllArgsConstructor
public class Category {
    private Long id;
    private String name;
}

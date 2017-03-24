package com.github.kickshare.kickstarter.entity;

import java.math.BigDecimal;
import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Jan.Kucera
 * @since 23.3.2017
 */
@Data
@AllArgsConstructor
public class Project {
    private BigDecimal id;
    private String name;
    private String description;
    private String url;

    private Instant deadline;

}

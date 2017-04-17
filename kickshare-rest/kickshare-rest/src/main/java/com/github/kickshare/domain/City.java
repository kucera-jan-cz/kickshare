package com.github.kickshare.domain;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Jan.Kucera
 * @since 16.4.2017
 */
@Data
@AllArgsConstructor
public class City {
    private Integer    id;
    private String     name;
    private BigDecimal lat;
    private BigDecimal lon;

    public City() {

    }
}

package com.github.kickshare.domain;

import java.util.Currency;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Jan.Kucera
 * @since 25.5.2017
 */
@Data
@AllArgsConstructor
public class Reward {
    private String name;
    private Long price;
    private Currency currency;
}

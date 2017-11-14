package com.github.kickshare.ext.service.currency;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Jan.Kucera
 * @since 14.11.2017
 */
@Getter
@AllArgsConstructor
public class Rate {
    private final String from;
    private final String to;
    private final Double amount;
    private final String source;
}

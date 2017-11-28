package com.github.kickshare.ext.service.kickstarter.reward;

import java.util.Currency;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * @author Jan.Kucera
 * @since 14.11.2017
 */
@AllArgsConstructor
@ToString
@Data
public class Price {
    private Double amount;
    private Currency currency;
}

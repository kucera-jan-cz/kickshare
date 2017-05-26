package com.github.kickshare.sheet;

import java.util.Currency;
import java.util.List;

import com.github.kickshare.domain.BackerInfo;
import com.github.kickshare.domain.Reward;
import lombok.Data;

/**
 * @author Jan.Kucera
 * @since 25.5.2017
 */
@Data
public class PledgeCalculationMetadata {
    private Currency fromCurrency;
    private Currency toCurrency;
    private Double conversionRate;
    private List<BackerInfo> backers;
    private List<Reward> rewards;
}

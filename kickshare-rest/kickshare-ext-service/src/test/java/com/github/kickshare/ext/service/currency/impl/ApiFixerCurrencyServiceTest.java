package com.github.kickshare.ext.service.currency.impl;

import static org.testng.Assert.assertEquals;

import java.io.InputStream;

import com.github.kickshare.ext.service.currency.CurrencyService;
import com.github.kickshare.ext.service.currency.Rate;
import org.testng.annotations.Test;

/**
 * @author Jan.Kucera
 * @since 14.11.2017
 */
public class ApiFixerCurrencyServiceTest {
    @Test
    public void testConvert() throws Exception {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("data/currency/api_fixer_usd_to_czk.json");
        CurrencyService service = new ApiFixerCurrencyService((String) -> inputStream);
        Rate rate = service.convert("USD", "CZK");
        assertEquals(rate.getFrom(), "USD");
        assertEquals(rate.getTo(), "CZK");
        assertEquals(rate.getAmount(), new Double(28.721));
        assertEquals(rate.getSource(), "API_FIXER");
    }

}
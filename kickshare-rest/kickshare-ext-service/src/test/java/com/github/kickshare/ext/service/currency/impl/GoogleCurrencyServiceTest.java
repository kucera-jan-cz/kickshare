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
public class GoogleCurrencyServiceTest {
    @Test
    public void testConvert() throws Exception {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("data/currency/google_usd_to_czk.html");
        CurrencyService service = new GoogleCurrencyService((String) -> inputStream);
        Rate rate = service.convert("USD", "CZK");
        assertEquals(rate.getFrom(), "USD");
        assertEquals(rate.getTo(), "CZK");
        assertEquals(rate.getAmount(), new Double(21.9599));
        assertEquals(rate.getSource(), "GOOGLE_FINANCE");
    }

}
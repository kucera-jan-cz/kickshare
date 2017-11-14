package com.github.kickshare.ext.service.currency.impl;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.fail;

import com.github.kickshare.ext.service.currency.CurrencyService;
import com.github.kickshare.ext.service.currency.Rate;
import com.github.kickshare.ext.service.exception.ServiceNotAvailable;
import org.testng.annotations.Test;

/**
 * @author Jan.Kucera
 * @since 14.11.2017
 */
public class FailOverCurrencyServiceTest {
    @Test
    public void testFirstServiceFailure() {
        CurrencyService fst = mock(CurrencyService.class);
        CurrencyService snd = mock(CurrencyService.class);
        FailOverCurrencyService service = new FailOverCurrencyService(fst, snd);
        when(fst.convert(anyString(), anyString())).thenThrow(new ServiceNotAvailable("First service failed"));
        when(snd.convert(anyString(), anyString())).thenReturn(new Rate("USD", "CZK", 25.1, "SECOND"));

        assertNotNull(service.convert("USD", "CZK"));
        verify(fst, times(1)).convert(anyString(), anyString());
        verify(snd, times(1)).convert(anyString(), anyString());
    }

    @Test(expectedExceptions = ServiceNotAvailable.class)
    public void testBothServiceFailure() {
        CurrencyService fst = mock(CurrencyService.class);
        CurrencyService snd = mock(CurrencyService.class);
        FailOverCurrencyService service = new FailOverCurrencyService(fst, snd);
        when(fst.convert(anyString(), anyString())).thenThrow(new ServiceNotAvailable("First service failed"));
        when(snd.convert(anyString(), anyString())).thenThrow(new ServiceNotAvailable("First service failed"));

        service.convert("USD", "CZK");
        fail("Service should have thrown exception");
    }
}
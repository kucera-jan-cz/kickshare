package com.github.kickshare.ext.service.currency;

import com.github.kickshare.ext.service.exception.ServiceNotAvailable;

/**
 * @author Jan.Kucera
 * @since 14.11.2017
 */
public interface CurrencyService {
    Rate convert(String from, String to) throws ServiceNotAvailable;

    String getIdentification();
}

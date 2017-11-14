package com.github.kickshare.ext.service.currency.impl;

import com.github.kickshare.ext.service.currency.CurrencyService;
import com.github.kickshare.ext.service.currency.Rate;
import com.github.kickshare.ext.service.exception.ServiceNotAvailable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jan.Kucera
 * @since 14.11.2017
 */
public class FailOverCurrencyService implements CurrencyService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FailOverCurrencyService.class);
    private final CurrencyService[] services;

    public FailOverCurrencyService(final CurrencyService... services) {
        this.services = services;
    }

    @Override
    public Rate convert(final String from, final String to) throws ServiceNotAvailable {
        for (CurrencyService service : services) {
            try {
                return service.convert(from, to);
            } catch (ServiceNotAvailable ex) {
                LOGGER.debug("Service {} failed", service.getIdentification(), ex);
            }
        }
        throw new ServiceNotAvailable("All services are not available");
    }

    @Override
    public String getIdentification() {
        return "FAIL_OVER";
    }
}

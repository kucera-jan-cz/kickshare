package com.github.kickshare.ext.service.currency.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.text.MessageFormat;
import java.util.InputMismatchException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kickshare.ext.service.common.ResourceBuilder;
import com.github.kickshare.ext.service.currency.CurrencyService;
import com.github.kickshare.ext.service.currency.Rate;
import com.github.kickshare.ext.service.exception.ServiceNotAvailable;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jan.Kucera
 * @since 14.11.2017
 */
@RequiredArgsConstructor
public class ApiFixerCurrencyService implements CurrencyService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiFixerCurrencyService.class);
    private static final String SERVICE_TYPE = "API_FIXER";
    private final ObjectMapper mapper = new ObjectMapper();
    private final ResourceBuilder resourceBuilder;

    @Override
    public Rate convert(final String from, final String to) throws ServiceNotAvailable {
        final String path = buildUrl(from, to);
        try (InputStream inputStream = resourceBuilder.apply(path)) {
            JsonNode root = mapper.readTree(inputStream);
            JsonNode rate = root.path("rates").path(to);
            if (!rate.isMissingNode()) {
                final Double value = rate.doubleValue();
                LOGGER.debug("{} -> {}: {}", from, to, value);
                return new Rate(from, to, value, SERVICE_TYPE);
            } else {
                throw new InputMismatchException("Currency pattern failed");
            }
        } catch (MalformedURLException e) {
            throw new ServiceNotAvailable("URL construction failed", e);
        } catch (IOException e) {
            throw new ServiceNotAvailable("Failed to retrieve service", e);
        } catch (RuntimeException ex) {
            throw new ServiceNotAvailable("Failed to parse service", ex);
        }
    }

    @Override
    public String getIdentification() {
        return SERVICE_TYPE;
    }

    private String buildUrl(final String from, final String to) {
        //https://api.fixer.io/latest?from=GBP&to=CZK&amount=1
        return MessageFormat.format("https://api.fixer.io/latest?from={0}&to={1}&amount=1", from, to);
    }
}

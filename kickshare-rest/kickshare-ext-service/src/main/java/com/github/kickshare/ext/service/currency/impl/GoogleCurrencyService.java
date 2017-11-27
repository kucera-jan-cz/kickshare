package com.github.kickshare.ext.service.currency.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.text.MessageFormat;
import java.util.InputMismatchException;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;

import com.github.kickshare.ext.service.currency.CurrencyService;
import com.github.kickshare.ext.service.currency.Rate;
import com.github.kickshare.ext.service.exception.ServiceNotAvailable;
import lombok.AllArgsConstructor;
import org.dom4j.Document;
import org.dom4j.io.DOMReader;
import org.dom4j.tree.DefaultElement;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jan.Kucera
 * @since 14.11.2017
 */
@AllArgsConstructor
public class GoogleCurrencyService implements CurrencyService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GoogleCurrencyService.class);
    private static final String RESULT_XPATH = "./descendant::div[@id='currency_converter_result']/span";
    private static final Pattern CURRENCY_PATTERN = Pattern.compile("((\\d+\\.?)+)\\s+(\\w+)");
    private static final String SERVICE_TYPE = "GOOGLE_FINANCE";
    private final Function<String, InputStream> resourceBuilder;

    @Override
    public Rate convert(final String from, final String to) {
        final String path = buildUrl(from, to);
        try (InputStream ins = resourceBuilder.apply(path)) {
            TagNode tagNode = new HtmlCleaner().clean(ins);
            DomSerializer serializer = new DomSerializer(new CleanerProperties());
            Document dom4j = new DOMReader().read(serializer.createDOM(tagNode));
            List<DefaultElement> divs = dom4j.selectNodes(RESULT_XPATH);
            String text = divs.get(0).getText();
            LOGGER.debug("XPATH search found: {}", text);
            Matcher matcher = CURRENCY_PATTERN.matcher(text);
            if (matcher.matches()) {
                Double value = Double.parseDouble(matcher.group(1));
                LOGGER.debug("{} -> {}: {}", from, to, value);
                return new Rate(from, to, value, SERVICE_TYPE);
            } else {
                throw new InputMismatchException("Currency pattern failed");
            }
        } catch (MalformedURLException e) {
            throw new ServiceNotAvailable("URL construction failed", e);
        } catch (IOException e) {
            throw new ServiceNotAvailable("Failed to retrieve service", e);
        } catch (ParserConfigurationException | RuntimeException ex) {
            throw new ServiceNotAvailable("Failed to parse service", ex);
        }
    }

    @Override
    public String getIdentification() {
        return SERVICE_TYPE;
    }

    private String buildUrl(String from, String to) {
        return MessageFormat.format("https://finance.google.com/finance/converter?a=1&from={0}&to={1}", from, to);
    }
}

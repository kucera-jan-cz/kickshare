package com.github.kickshare;

import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dom4j.Document;
import org.dom4j.io.DOMReader;
import org.dom4j.tree.DefaultElement;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

/**
 * @author Jan.Kucera
 * @since 9.11.2017
 */
public class CurrencyServiceTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(CurrencyServiceTest.class);
    private static final String RESULT_XPATH = "./descendant::div[@id='currency_converter_result']/span";
    private static final Pattern CURRENCY_PATTERN = Pattern.compile("((\\d+\\.?)+)\\s+(\\w+)");

    @Test
    public void parseGoogleCurrency() throws IOException, ParserConfigurationException {
        //https://finance.google.com/finance/converter?a=1&from=USD&to=CZK
        InputStream html = this.getClass().getClassLoader().getResourceAsStream("data/currency/google_usd_to_czk.html");
        TagNode tagNode = new HtmlCleaner().clean(html);
        DomSerializer serializer = new DomSerializer(new CleanerProperties());
        Document dom4j = new DOMReader().read(serializer.createDOM(tagNode));
        List<DefaultElement> divs = dom4j.selectNodes(RESULT_XPATH);
        String text = divs.get(0).getText();
        LOGGER.info("{}", text);
        Matcher matcher = CURRENCY_PATTERN.matcher(text);
        assertTrue(matcher.matches());
        Double value = Double.parseDouble(matcher.group(1));
        LOGGER.info("{}", value);
    }

    @Test
    public void parseApiFixerCurrency() throws IOException {
        //https://api.fixer.io/latest?from=GBP&to=CZK&amount=1
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("data/currency/api_fixer_usd_to_czk.json")) {
            JsonNode root = mapper.readTree(inputStream);
            final Double rate = root.path("rates").path("CZK").asDouble(Double.MIN_VALUE);
            LOGGER.info("USD -> CZK: {}", rate);
        }


    }
}

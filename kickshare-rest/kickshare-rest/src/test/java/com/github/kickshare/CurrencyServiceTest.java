package com.github.kickshare;

import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;

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
    public void parseCurrency() throws IOException, ParserConfigurationException {
        InputStream html = this.getClass().getClassLoader().getResourceAsStream("data/currency/usd_to_czk.html");
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
}

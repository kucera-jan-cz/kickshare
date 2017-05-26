package com.github.kickshare;

import java.io.InputStream;
import java.util.Currency;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringEscapeUtils;
import org.dom4j.Document;
import org.dom4j.Node;
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
 * @since 19.3.2017
 */
public class RewardServiceTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(RewardServiceTest.class);

    private static final String PRICE_REGEX = "(\\D+)\\s*([^a-zA-Z]+)";
    private static final Pattern PRICE_PATTERN = Pattern.compile(PRICE_REGEX);

    private static final String REWARDS_XPATH = "//li[@data-reward-id]";
    private static final String REWARD_MONEY_XPATH = "./descendant::span[@class='money']";
    public static final String REWARD_TITLE_XPATH = "./descendant::h3[@class='pledge__title']";

    private static final Map<String, Currency> currenciesByCode = Currency.getAvailableCurrencies().stream()
            .collect(Collectors.toMap(Currency::getCurrencyCode, Function.identity()));

    @Test
    public void parseProfile() throws Exception {
        InputStream html = this.getClass().getClassLoader().getResourceAsStream("data/kickstarter/campaigns/quodd_heroes.html");
//        InputStream html = this.getClass().getClassLoader().getResourceAsStream("data/kickstarter/campaigns/edge.html");
//        InputStream html = this.getClass().getClassLoader().getResourceAsStream("data/kickstarter/campaigns/badass_riders.html");
        TagNode tagNode = new HtmlCleaner().clean(html);
        DomSerializer serializer = new DomSerializer(new CleanerProperties());
        Document dom4j = new DOMReader().read(serializer.createDOM(tagNode));
        Currency currency = currenciesByCode.get("GBP");

        List<DefaultElement> rewards = dom4j.selectNodes(REWARDS_XPATH);
        for (DefaultElement reward : rewards) {
            Node moneyElement = reward.selectSingleNode(REWARD_MONEY_XPATH);
            String moneyText = moneyElement.getText().trim();
            Matcher matcher = PRICE_PATTERN.matcher(StringEscapeUtils.unescapeHtml4(moneyText));
            if (!matcher.matches()) {
                throw new IllegalArgumentException("Could not parse currency and reward price: " + moneyText);
            }
            String priceAsText = matcher.group(2).replaceAll(",", "").replaceAll(",", "").replaceAll(" ", "");
            Long price = Long.parseLong(priceAsText);


            Node titleElement = reward.selectSingleNode(REWARD_TITLE_XPATH);
            String titleText = titleElement.getText().trim();
            LOGGER.info("Title: {}", titleText);
            LOGGER.info("Price: {} {}", price, currency);
        }
    }
}

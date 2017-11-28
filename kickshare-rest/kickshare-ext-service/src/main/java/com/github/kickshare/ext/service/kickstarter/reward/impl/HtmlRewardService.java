package com.github.kickshare.ext.service.kickstarter.reward.impl;

import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kickshare.ext.service.common.ResourceBuilder;
import com.github.kickshare.ext.service.exception.ServiceNotAvailable;
import com.github.kickshare.ext.service.kickstarter.reward.Price;
import com.github.kickshare.ext.service.kickstarter.reward.Reward;
import com.github.kickshare.ext.service.kickstarter.reward.RewardService;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.DOMReader;
import org.dom4j.tree.DefaultElement;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.CleanerTransformations;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.TagTransformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * @author Jan.Kucera
 * @since 14.11.2017
 */
public class HtmlRewardService implements RewardService {
    private static final Logger LOGGER = LoggerFactory.getLogger(HtmlRewardService.class);
    private static final String PRICE_REGEX = "(\\D+)\\s*([^a-zA-Z]+)";
    private static final Pattern PRICE_PATTERN = Pattern.compile(PRICE_REGEX);

    private static final String REWARDS_XPATH = "//li[@data-reward-id]";
    private static final String REWARD_MONEY_XPATH = "./descendant::span[@class='money']";
    private static final String REWARD_TITLE_XPATH = "./descendant::h3[@class='pledge__title']";
    private static final String REWARD_DESCRIPTION_XPATH = "./descendant::div[contains(@class, 'pledge__reward-description')]";
    private static final NumberFormat US_FORMAT = NumberFormat.getNumberInstance(Locale.US);
    private static final Map<String, Currency> currenciesByCode = Currency.getAvailableCurrencies().stream()
            .collect(Collectors.toMap(Currency::getCurrencyCode, Function.identity()));
    private static final TypeReference<Map<String, String>> MAP = new TypeReference<Map<String, String>>() {};
    private final Map<String, String> currenciesBySymbols;

    private final ResourceBuilder resourceBuilder;
    private final DomSerializer serializer;

    public HtmlRewardService(final ResourceBuilder resourceBuilder) {
        this.resourceBuilder = resourceBuilder;

        final CleanerProperties properties = new CleanerProperties();
        final CleanerTransformations transformations = new CleanerTransformations();
        transformations.addTransformation(new TagTransformation("p"));
        transformations.addTransformation(new TagTransformation("br"));
        properties.setCleanerTransformations(transformations);
        this.serializer = new DomSerializer(properties);
        try {
            InputStream ins = new ClassPathResource("currency_symbol_mapping.json").getInputStream();
            currenciesBySymbols = MapUtils.invertMap(new ObjectMapper().readValue(ins, MAP));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public List<Reward> getRewards(final String path) {
        try (InputStream ins = resourceBuilder.apply(path)) {
            TagNode tagNode = new HtmlCleaner().clean(ins);
            Document dom4j = new DOMReader().read(serializer.createDOM(tagNode));
            return parseRewards(dom4j);
        } catch (ParserConfigurationException | IOException ex) {
            throw new ServiceNotAvailable("Failed to retrieve and parse rewards", ex);
        }
    }

    private List<Reward> parseRewards(final Document dom4j) {
        List<DefaultElement> rewards = dom4j.selectNodes(REWARDS_XPATH);
        return rewards.stream().map(this::parseReward).collect(Collectors.toList());
    }

    private Reward parseReward(DefaultElement rewardElement) {
        Node moneyElement = rewardElement.selectSingleNode(REWARD_MONEY_XPATH);
        String moneyText = moneyElement.getText().trim();
        Matcher matcher = PRICE_PATTERN.matcher(StringEscapeUtils.unescapeHtml4(moneyText));
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Could not parse currency and reward price: " + moneyText);
        }
        Currency currency = parseCurrency(matcher.group(1));
        Double price = parseAmount(matcher.group(2));

        Node titleElement = rewardElement.selectSingleNode(REWARD_TITLE_XPATH);
        String titleText = titleElement.getText().trim();
        Node descriptionElement = rewardElement.selectSingleNode(REWARD_DESCRIPTION_XPATH);
        List<Node> descriptionParams = descriptionElement.selectNodes("./p");
        String description = descriptionParams.stream()
                .map(Node::getText)
                .filter(StringUtils::isNotBlank)
                .map(s -> s.replaceAll("^\\s+|\\s+$|\\s*(\n)\\s*|(\\s)\\s*", "$1$2"))
                .collect(Collectors.joining(System.lineSeparator()));

        LOGGER.info("Description:\t{}", description);
        Reward reward = new Reward(titleText, new Price(price, Currency.getInstance("USD")), description);
        LOGGER.info("Parsed reward: {}", reward);
        return reward;
    }

    private Currency parseCurrency(String currencyAsText) {
        String type = currenciesBySymbols.get(currencyAsText);
        return currenciesByCode.get(type);
    }

    private Double parseAmount(String priceAsText) {
        try {
            return US_FORMAT.parse(priceAsText).doubleValue();
        } catch (ParseException e) {
            return Double.parseDouble(priceAsText);
        }
    }
}

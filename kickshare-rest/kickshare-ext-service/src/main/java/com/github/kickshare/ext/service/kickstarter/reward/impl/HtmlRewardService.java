package com.github.kickshare.ext.service.kickstarter.reward.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Currency;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;

import com.github.kickshare.ext.service.exception.ServiceNotAvailable;
import com.github.kickshare.ext.service.kickstarter.reward.Price;
import com.github.kickshare.ext.service.kickstarter.reward.Reward;
import com.github.kickshare.ext.service.kickstarter.reward.RewardService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
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

/**
 * @author Jan.Kucera
 * @since 14.11.2017
 */
@RequiredArgsConstructor
public class HtmlRewardService implements RewardService {
    private static final Logger LOGGER = LoggerFactory.getLogger(HtmlRewardService.class);
    private static final String PRICE_REGEX = "(\\D+)\\s*([^a-zA-Z]+)";
    private static final Pattern PRICE_PATTERN = Pattern.compile(PRICE_REGEX);

    private static final String REWARDS_XPATH = "//li[@data-reward-id]";
    private static final String REWARD_MONEY_XPATH = "./descendant::span[@class='money']";
    public static final String REWARD_TITLE_XPATH = "./descendant::h3[@class='pledge__title']";
    public static final String REWARD_DESCRIPTION_XPATH = "./descendant::div[contains(@class, 'pledge__reward-description')]";

    private final Function<String, InputStream> resourceBuilder;

    @Override
    public List<Reward> getRewards(final String path) {
        try (InputStream ins = resourceBuilder.apply(path)) {
            TagNode tagNode = new HtmlCleaner().clean(ins);
            DomSerializer serializer = new DomSerializer(new CleanerProperties());
            Document dom4j = new DOMReader().read(serializer.createDOM(tagNode));
            parseRewards(dom4j);
        } catch (ParserConfigurationException | IOException ex) {
            throw new ServiceNotAvailable("Failed to retrieve and parse rewards", ex);
        }
        return null;
    }

    private List<Reward> parseRewards(final Document dom4j) {
        List<DefaultElement> rewards = dom4j.selectNodes(REWARDS_XPATH);
        return rewards.stream().map(this::parseReward).collect(Collectors.toList());
    }

    private Reward parseReward(DefaultElement reward) {
        Node moneyElement = reward.selectSingleNode(REWARD_MONEY_XPATH);
        String moneyText = moneyElement.getText().trim();
        Matcher matcher = PRICE_PATTERN.matcher(StringEscapeUtils.unescapeHtml4(moneyText));
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Could not parse currency and reward price: " + moneyText);
        }
        String priceAsText = matcher.group(2).replaceAll(",", "").replaceAll(",", "").replaceAll(" ", "");
        Double price = Double.parseDouble(priceAsText);


        Node titleElement = reward.selectSingleNode(REWARD_TITLE_XPATH);
        String titleText = titleElement.getText().trim();
        Node descriptionElement = reward.selectSingleNode(REWARD_DESCRIPTION_XPATH);
        List<Node> descriptionParams = descriptionElement.selectNodes("./p");
        //@TODO - clean text formatting, use regex (https://stackoverflow.com/questions/15494780/remove-all-whitespaces-from-string-but-keep-one-newline)
        //@TODO - or study whether HtmlCleaner could help
        String description = descriptionParams.stream()
                .map(Node::getText)
                .map(String::trim)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.joining(System.lineSeparator()));
        LOGGER.info("Title: {}", titleText);
        LOGGER.info("Price: {}", price);
        LOGGER.info("Description:\n{}", description);
        return new Reward(titleText, new Price(price, Currency.getInstance("USD")), null);
    }
}

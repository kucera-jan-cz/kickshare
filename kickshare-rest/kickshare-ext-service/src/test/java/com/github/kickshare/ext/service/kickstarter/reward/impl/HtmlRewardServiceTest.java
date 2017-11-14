package com.github.kickshare.ext.service.kickstarter.reward.impl;

import java.io.InputStream;

import org.testng.annotations.Test;

/**
 * @author Jan.Kucera
 * @since 14.11.2017
 */
public class HtmlRewardServiceTest {

    @Test
    public void parse() {
        InputStream html = this.getClass().getClassLoader().getResourceAsStream("data/kickstarter/campaigns/quodd_heroes.html");
        HtmlRewardService service = new HtmlRewardService((String) -> html);
        service.getRewards("");
    }
}
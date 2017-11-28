package com.github.kickshare.ext.service.kickstarter.reward.impl;

import static org.testng.Assert.assertEquals;

import java.io.InputStream;
import java.util.List;

import com.github.kickshare.ext.service.common.ResourceStream;
import com.github.kickshare.ext.service.kickstarter.reward.Reward;
import org.testng.annotations.Test;

/**
 * @author Jan.Kucera
 * @since 14.11.2017
 */
public class HtmlRewardServiceTest {

    @Test
    public void parse() {
        InputStream html = ResourceStream.of("data/kickstarter/campaigns/quodd_heroes.html");
        HtmlRewardService service = new HtmlRewardService((String) -> html);
        List<Reward> rewards = service.getRewards("");
        Reward reward = rewards.stream().filter(r -> "Super Limited Edition".equals(r.getTitle())).findFirst().get();
        assertEquals(reward.getPrice().getAmount().intValue(), 500);
    }
}
package com.github.kickshare.ext.service.kickstarter.reward;

import java.util.List;

import com.github.kickshare.ext.service.exception.ServiceNotAvailable;

/**
 * @author Jan.Kucera
 * @since 14.11.2017
 */
public interface RewardService {
    List<Reward> getRewards(String path) throws ServiceNotAvailable;
}

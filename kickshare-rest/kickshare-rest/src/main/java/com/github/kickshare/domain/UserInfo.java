package com.github.kickshare.domain;

import lombok.Data;

/**
 * @author Jan.Kucera
 * @since 19.3.2017
 */
@Data
public class UserInfo {
    private Backer backer;
    private Address address;
}

package com.github.kickshare.domain;

import lombok.Data;

/**
 * @author Jan.Kucera
 * @since 19.3.2017
 */
@Data
public class UserInfo {
    private User backer;
    private Address address;
}

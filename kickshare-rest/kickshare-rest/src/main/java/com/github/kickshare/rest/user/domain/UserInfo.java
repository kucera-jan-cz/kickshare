package com.github.kickshare.rest.user.domain;

import lombok.Data;

/**
 * @author Jan.Kucera
 * @since 19.3.2017
 */
@Data
public class UserInfo {
    private String email;

    private String name;
    private String surname;

    private String ksLogin;

    private Address address;


}

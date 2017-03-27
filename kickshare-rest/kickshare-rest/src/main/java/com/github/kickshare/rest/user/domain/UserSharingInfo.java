package com.github.kickshare.rest.user.domain;

import java.util.List;

/**
 * @author Jan.Kucera
 * @since 19.3.2017
 */
public class UserSharingInfo {

    private List<String> pickUpCities;
    private String favoriteCategory;

    //notifications
    private Boolean notifiedOnlyToNearBy;
    private Integer distanceLimit;
    private List<String> tags;
}

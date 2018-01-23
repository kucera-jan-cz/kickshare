package com.github.kickshare.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Jan.Kucera
 * @since 7.11.2017
 */
@AllArgsConstructor
@Component
public class EntityMapper {
    private static EntityMapper INSTANCE;

    private AddressMapper address;
    private CategoryMapper category;
    private BackerMapper backer;
    private GroupMapper group;
    private ProjectMapper project;
    private ProjectPhotoMapper photo;
    private TagMapper tag;
    private CityMapper city;
    private NotificationMapper notification;
    private SimpleMapper simple;


    public static AddressMapper address() {
        return INSTANCE.address;
    }

    public static CategoryMapper category() {
        return INSTANCE.category;
    }

    public static BackerMapper backer() {
        return INSTANCE.backer;
    }

    public static GroupMapper group() {
        return INSTANCE.group;
    }

    public static ProjectMapper project() {
        return INSTANCE.project;
    }

    public static ProjectPhotoMapper photo() {
        return INSTANCE.photo;
    }

    public static TagMapper tag() {
        return INSTANCE.tag;
    }

    public static CityMapper city() {
        return INSTANCE.city;
    }

    public static NotificationMapper notification() {
        return INSTANCE.notification;
    }

    public static SimpleMapper map() {
        return INSTANCE.simple;
    }

    public static void setInstance(final EntityMapper instance) {
        EntityMapper.INSTANCE = instance;
    }
}

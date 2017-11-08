package com.github.kickshare.mapper;

import org.mapstruct.factory.Mappers;

/**
 * @author Jan.Kucera
 * @since 7.11.2017
 */
public class EntityMapper {
    private static AddressMapper address = Mappers.getMapper(AddressMapper.class);
    private static BackerMapper backer = Mappers.getMapper(BackerMapper.class);
    private static GroupMapper group = Mappers.getMapper(GroupMapper.class);
    private static ProjectMapper project = Mappers.getMapper(ProjectMapper.class);
    private static ProjectPhotoMapper photo = Mappers.getMapper(ProjectPhotoMapper.class);
    private static TagMapper tag = Mappers.getMapper(TagMapper.class);
    private static NotificationMapper notification = Mappers.getMapper(NotificationMapper.class);

    public static AddressMapper address() {
        return address;
    }

    public static BackerMapper backer() {
        return backer;
    }

    public static GroupMapper group() {
        return group;
    }

    public static ProjectMapper project() {
        return project;
    }

    public static ProjectPhotoMapper photo() {
        return photo;
    }

    public static TagMapper tag() {
        return tag;
    }

    public static NotificationMapper notification() {
        return notification;
    }
}

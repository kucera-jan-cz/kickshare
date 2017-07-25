package com.github.kickshare.mapper;

import java.util.List;

import com.github.kickshare.domain.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author Jan.Kucera
 * @since 6.6.2017
 */
@Mapper(config = CentralConfig.class)
public interface NotificationMapper {

    NotificationMapper MAPPER = Mappers.getMapper(NotificationMapper.class);

    Notification toDomain(com.github.kickshare.db.jooq.tables.pojos.Notification source);

    List<Notification> toDomain(List<com.github.kickshare.db.jooq.tables.pojos.Notification> source);

    com.github.kickshare.db.jooq.tables.pojos.Notification toDB(Notification source);

    List<com.github.kickshare.db.jooq.tables.pojos.Notification> toDB(List<Notification> source);

}

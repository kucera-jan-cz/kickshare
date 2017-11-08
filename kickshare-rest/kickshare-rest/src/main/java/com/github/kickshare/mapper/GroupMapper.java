package com.github.kickshare.mapper;

import com.github.kickshare.domain.Group;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * @author Jan.Kucera
 * @since 6.6.2017
 */
@Mapper(config = CentralConfig.class)
public interface GroupMapper {

    Group toDomain(com.github.kickshare.db.jooq.tables.pojos.Group source);

    @Mappings({
            @Mapping(target = "lat", ignore = true),
            @Mapping(target = "cityId", ignore = true),
            @Mapping(target = "lon", ignore = true),
            @Mapping(target = "isLocal", ignore = true)
    })
    com.github.kickshare.db.jooq.tables.pojos.Group toDB(Group source);

}

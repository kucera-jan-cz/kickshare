package com.github.kickshare.mapper;

import com.github.kickshare.domain.GroupSummary;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * @author Jan.Kucera
 * @since 6.6.2017
 */
//@Mapper(config = CentralConfig.class)
public interface GroupMapper {

    GroupMapper MAPPER = Mappers.getMapper(GroupMapper.class);

    @Mappings({
    })
    GroupSummary toDomain(com.github.kickshare.db.jooq.tables.pojos.Group source);

    com.github.kickshare.db.jooq.tables.pojos.Group toDB(GroupSummary source);

}

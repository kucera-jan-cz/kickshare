package com.github.kickshare.mapper;

import com.github.kickshare.domain.Backer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author Jan.Kucera
 * @since 11.8.2017
 */
@Mapper(config = CentralConfig.class)
public interface BackerMapper {
    BackerMapper MAPPER = Mappers.getMapper(BackerMapper.class);

    Backer toDomain(com.github.kickshare.db.jooq.tables.pojos.Backer source);

    com.github.kickshare.db.jooq.tables.pojos.Backer toDB(Backer source);
}

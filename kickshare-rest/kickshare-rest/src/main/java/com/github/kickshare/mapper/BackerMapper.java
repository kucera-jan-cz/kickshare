package com.github.kickshare.mapper;

import com.github.kickshare.domain.Backer;
import org.mapstruct.Mapper;

/**
 * @author Jan.Kucera
 * @since 11.8.2017
 */
@Mapper(config = CentralConfig.class)
public interface BackerMapper {
    Backer toDomain(com.github.kickshare.db.jooq.tables.pojos.Backer source);

    com.github.kickshare.db.jooq.tables.pojos.Backer toDB(Backer source);
}

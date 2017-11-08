package com.github.kickshare.mapper;

import com.github.kickshare.domain.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author Jan.Kucera
 * @since 17.7.2017
 */
@Mapper(config = CentralConfig.class)
public interface TagMapper {
    TagMapper INSTANCE = Mappers.getMapper(TagMapper.class);

    Tag toDomain(com.github.kickshare.db.jooq.tables.pojos.TagDB source);

    com.github.kickshare.db.jooq.tables.pojos.TagDB toDB(Tag source);
}

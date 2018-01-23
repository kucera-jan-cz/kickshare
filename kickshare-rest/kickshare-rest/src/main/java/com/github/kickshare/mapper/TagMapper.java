package com.github.kickshare.mapper;

import java.util.List;

import com.github.kickshare.db.jooq.tables.pojos.TagDB;
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

    Tag toDomain(TagDB source);

    List<Tag> toDomain(List<TagDB> source);

    TagDB toDB(Tag source);

    List<TagDB> toDB(List<Tag> source);
}

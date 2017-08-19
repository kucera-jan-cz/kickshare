package com.github.kickshare.mapper;

import com.github.kickshare.domain.Address;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author Jan.Kucera
 * @since 11.8.2017
 */
@Mapper(config = CentralConfig.class)
public interface AddressMapper {
    AddressMapper MAPPER = Mappers.getMapper(AddressMapper.class);

    Address toDomain(com.github.kickshare.db.jooq.tables.pojos.Address source);

    com.github.kickshare.db.jooq.tables.pojos.Address toDB(Address source);
}

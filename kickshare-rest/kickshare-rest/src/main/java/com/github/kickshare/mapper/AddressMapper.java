package com.github.kickshare.mapper;

import com.github.kickshare.domain.Address;
import org.mapstruct.Mapper;

/**
 * @author Jan.Kucera
 * @since 11.8.2017
 */
@Mapper(config = CentralConfig.class)
public interface AddressMapper {
    Address toDomain(com.github.kickshare.db.jooq.tables.pojos.AddressDB source);

    com.github.kickshare.db.jooq.tables.pojos.AddressDB toDB(Address source);
}

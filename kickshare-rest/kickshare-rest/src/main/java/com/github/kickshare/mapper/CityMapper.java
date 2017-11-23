package com.github.kickshare.mapper;

import java.util.List;

import com.github.kickshare.db.jooq.tables.pojos.CityDB;
import com.github.kickshare.domain.City;
import org.mapstruct.Mapper;

/**
 * @author Jan.Kucera
 * @since 11.8.2017
 */
@Mapper(config = CentralConfig.class)
public interface CityMapper {
    City toDomain(CityDB source);

    CityDB toDB(City source);

    List<City> toDomain(List<CityDB> source);
}

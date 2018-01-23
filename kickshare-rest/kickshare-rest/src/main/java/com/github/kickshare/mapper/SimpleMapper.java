package com.github.kickshare.mapper;

import com.github.kickshare.db.query.SearchOptionsDB;
import com.github.kickshare.service.entity.SearchOptions;
import org.mapstruct.Mapper;

/**
 * @author Jan.Kucera
 */
@Mapper(config = CentralConfig.class)
public interface SimpleMapper {
    SearchOptionsDB toDB(SearchOptions source);
}

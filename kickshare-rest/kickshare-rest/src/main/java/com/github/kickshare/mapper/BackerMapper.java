package com.github.kickshare.mapper;

import java.util.List;

import com.github.kickshare.db.jooq.tables.pojos.BackerDB;
import com.github.kickshare.db.jooq.tables.pojos.LeaderDB;
import com.github.kickshare.domain.Backer;
import com.github.kickshare.domain.Leader;
import org.mapstruct.Mapper;

/**
 * @author Jan.Kucera
 * @since 11.8.2017
 */
@Mapper(config = CentralConfig.class)
public interface BackerMapper {
    //Backer
    Backer toDomain(BackerDB source);

    List<Backer> toDomain(List<BackerDB> source);

    BackerDB toDB(Backer source);

    List<BackerDB> toDB(List<Backer> source);

    //Leader
    Leader toDomain(LeaderDB source);

    LeaderDB toDB(Leader source);

}

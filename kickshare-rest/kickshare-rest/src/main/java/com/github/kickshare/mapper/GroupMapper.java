package com.github.kickshare.mapper;

import java.util.List;

import com.github.kickshare.db.jooq.tables.pojos.GroupDB;
import com.github.kickshare.db.jooq.tables.pojos.GroupPostDB;
import com.github.kickshare.domain.Group;
import com.github.kickshare.domain.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * @author Jan.Kucera
 * @since 6.6.2017
 */
@Mapper(config = CentralConfig.class)
public interface GroupMapper { //extends DomainToDB<GroupDB, Group> {
    //Group
    Group toDomain(GroupDB source);

    List<Group> toDomain(List<GroupDB> source);

    @Mappings({
            @Mapping(target = "lat", ignore = true),
            @Mapping(target = "cityId", ignore = true),
            @Mapping(target = "lon", ignore = true),
            @Mapping(target = "isLocal", ignore = true)
    })
    GroupDB toDB(Group source);

    List<GroupDB> toDB(List<Group> source);

    //Post
    Post toDomain(GroupPostDB source);

    List<Post> postsToDomain(List<GroupPostDB> source);

    GroupPostDB toDB(Post source);

    List<GroupPostDB> postsToDB(List<Post> source);

}

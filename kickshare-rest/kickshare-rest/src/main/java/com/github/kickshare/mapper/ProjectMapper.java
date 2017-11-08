package com.github.kickshare.mapper;

import com.github.kickshare.domain.Project;
import com.github.kickshare.domain.ProjectInfo;
import com.github.kickshare.domain.ProjectPhoto;
import com.github.kickshare.kickstarter.entity.CampaignProject;
import com.github.kickshare.kickstarter.entity.CampaignProjectPhoto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * @author Jan.Kucera
 * @since 6.6.2017
 */
@Mapper(config = CentralConfig.class)
public interface ProjectMapper {
    Project toDomain(com.github.kickshare.db.jooq.tables.pojos.Project source);

    @Mappings({
            @Mapping(source = "photo.small", target = "photoUrl"),
            @Mapping(source = "photo", target = "photo"),
            @Mapping(target = "project", ignore = true)
    })
    ProjectInfo toDomain(CampaignProject source);

    com.github.kickshare.db.jooq.tables.pojos.Project toDB(Project source);

    @Mapping(target = "photo", ignore = true)
    CampaignProject toKS(Project source);

    @Mapping(source = "id", target = "projectId")
    ProjectPhoto toDomain(CampaignProjectPhoto photo);
}

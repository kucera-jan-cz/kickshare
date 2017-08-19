package com.github.kickshare.mapper;

import com.github.kickshare.domain.ProjectPhoto;
import com.github.kickshare.kickstarter.entity.CampaignProjectPhoto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author Jan.Kucera
 * @since 3.8.2017
 */
@Mapper(config = CentralConfig.class)
public interface ProjectPhotoMapper {
    ProjectPhotoMapper INSTANCE = Mappers.getMapper(ProjectPhotoMapper.class);

    ProjectPhoto toDomain(com.github.kickshare.db.jooq.tables.pojos.ProjectPhoto source);

    com.github.kickshare.db.jooq.tables.pojos.ProjectPhoto toDB(ProjectPhoto source);

    @Mapping(source = "id", target = "projectId")
    ProjectPhoto toDomain(CampaignProjectPhoto source);

    @Mapping(source = "projectId", target = "id")
    CampaignProjectPhoto toKS(ProjectPhoto source);
}
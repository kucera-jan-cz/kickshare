package com.github.kickshare.mapper;

import com.github.kickshare.domain.ProjectPhoto;
import com.github.kickshare.ext.service.kickstarter.campaign.entity.CampaignProjectPhoto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Jan.Kucera
 * @since 3.8.2017
 */
@Mapper(config = CentralConfig.class)
public interface ProjectPhotoMapper {
    ProjectPhoto toDomain(com.github.kickshare.db.jooq.tables.pojos.ProjectPhotoDB source);

    com.github.kickshare.db.jooq.tables.pojos.ProjectPhotoDB toDB(ProjectPhoto source);

    @Mapping(source = "id", target = "projectId")
    ProjectPhoto toDomain(CampaignProjectPhoto source);

    @Mapping(source = "projectId", target = "id")
    CampaignProjectPhoto toKS(ProjectPhoto source);
}
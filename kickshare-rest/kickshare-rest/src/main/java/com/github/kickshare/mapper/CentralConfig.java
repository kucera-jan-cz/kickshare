package com.github.kickshare.mapper;

import org.mapstruct.MapperConfig;
import org.mapstruct.MappingInheritanceStrategy;
import org.mapstruct.ReportingPolicy;

/**
 * @author Jan.Kucera
 * @since 9.6.2017
 */
@MapperConfig(
        uses = DateMapper.class,
        componentModel= "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        mappingInheritanceStrategy = MappingInheritanceStrategy.AUTO_INHERIT_FROM_CONFIG
)
public interface CentralConfig {
}

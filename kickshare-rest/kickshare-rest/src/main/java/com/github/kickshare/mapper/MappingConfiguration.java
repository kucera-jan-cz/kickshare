package com.github.kickshare.mapper;

import org.dozer.Mapper;
import org.dozer.spring.DozerBeanMapperFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

/**
 * @author Jan.Kucera
 * @since 4.4.2017
 */
@Configuration
@ComponentScan("com.github.kickshare.mapper")
public class MappingConfiguration {

    @Bean
    public DozerBeanMapperFactoryBean dozerFactory(@Value("classpath:dozer/*mappings.xml") Resource[] resources) {
        final DozerBeanMapperFactoryBean dozerBeanMapperFactoryBean = new DozerBeanMapperFactoryBean();
        // Other configurations
        dozerBeanMapperFactoryBean.setMappingFiles(resources);
        return dozerBeanMapperFactoryBean;
    }

    @Bean
    public ExtendedMapper extendedMapper(@Autowired Mapper mapper) {
        return new ExtendedMapper(mapper);
    }

    @Autowired
    public void initEntityMapper(EntityMapper mapper) {
        EntityMapper.setInstance(mapper);
    }
}

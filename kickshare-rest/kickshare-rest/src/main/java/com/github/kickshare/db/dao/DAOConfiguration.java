package com.github.kickshare.db.dao;

import com.github.kickshare.db.h2.tables.daos.ProjectDao;
import com.github.kickshare.db.h2.tables.daos.ProjectPhotoDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Jan.Kucera
 * @since 10.4.2017
 */
@Configuration
public class DAOConfiguration {
    @Bean
    public ProjectDao projectDao(org.jooq.Configuration configuration) {
        return new ProjectDao(configuration);
    }

    @Bean
    public ProjectPhotoDao projectPhotoDao(org.jooq.Configuration configuration) {
        return new ProjectPhotoDao(configuration);
    }
}

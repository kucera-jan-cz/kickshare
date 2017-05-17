package com.github.kickshare.db.dao;

import com.github.kickshare.db.h2.tables.daos.AddressDao;
import com.github.kickshare.db.h2.tables.daos.BackerDao;
import com.github.kickshare.db.h2.tables.daos.BackerLocationsDao;
import com.github.kickshare.db.h2.tables.daos.Backer_2GroupDao;
import com.github.kickshare.db.h2.tables.daos.CategoryDao;
import com.github.kickshare.db.h2.tables.daos.CityDao;
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

    @Bean
    public CategoryDao categoryDao(org.jooq.Configuration configuration) {
        return new CategoryDao(configuration);
    }

    @Bean
    public BackerDao backerDao(org.jooq.Configuration configuration) {
        return new BackerDao(configuration);
    }

    @Bean
    public CityDao cityDao(org.jooq.Configuration configuration) {
        return new CityDao(configuration);
    }

    @Bean
    public AddressDao addressDao(org.jooq.Configuration configuration) {
        return new AddressDao(configuration);
    }

    @Bean
    public BackerLocationsDao locationsDao(org.jooq.Configuration configuration) {
        return new BackerLocationsDao(configuration);
    }

    @Bean
    public Backer_2GroupDao backerToGroupDao(org.jooq.Configuration configuration) {
        return new Backer_2GroupDao(configuration);
    }
}

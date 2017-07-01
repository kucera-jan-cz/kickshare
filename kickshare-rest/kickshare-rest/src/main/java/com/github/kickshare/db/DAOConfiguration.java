package com.github.kickshare.db;

import com.github.kickshare.db.jooq.tables.daos.AddressDao;
import com.github.kickshare.db.jooq.tables.daos.BackerDao;
import com.github.kickshare.db.jooq.tables.daos.BackerLocationDao;
import com.github.kickshare.db.jooq.tables.daos.Backer_2GroupDao;
import com.github.kickshare.db.jooq.tables.daos.CategoryDao;
import com.github.kickshare.db.jooq.tables.daos.CityDao;
import com.github.kickshare.db.jooq.tables.daos.LeaderDao;
import com.github.kickshare.db.jooq.tables.daos.ProjectDao;
import com.github.kickshare.db.jooq.tables.daos.ProjectPhotoDao;
import com.github.kickshare.db.jooq.tables.daos.UsersDao;
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
    public BackerLocationDao locationsDao(org.jooq.Configuration configuration) {
        return new BackerLocationDao(configuration);
    }

    @Bean
    public Backer_2GroupDao backerToGroupDao(org.jooq.Configuration configuration) {
        return new Backer_2GroupDao(configuration);
    }

    @Bean
    public LeaderDao leaderDao(org.jooq.Configuration configuration) {
        return new LeaderDao(configuration);
    }

    @Bean
    public UsersDao usersDao(org.jooq.Configuration configuration) {
        return new UsersDao(configuration);
    }
}

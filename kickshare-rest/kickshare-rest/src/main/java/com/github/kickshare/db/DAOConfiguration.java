package com.github.kickshare.db;

import com.github.kickshare.db.jooq.tables.daos.AddressDaoDB;
import com.github.kickshare.db.jooq.tables.daos.BackerDaoDB;
import com.github.kickshare.db.jooq.tables.daos.BackerLocationDaoDB;
import com.github.kickshare.db.jooq.tables.daos.Backer_2GroupDaoDB;
import com.github.kickshare.db.jooq.tables.daos.CategoryDaoDB;
import com.github.kickshare.db.jooq.tables.daos.CityDaoDB;
import com.github.kickshare.db.jooq.tables.daos.GroupPostDaoDB;
import com.github.kickshare.db.jooq.tables.daos.LeaderDaoDB;
import com.github.kickshare.db.jooq.tables.daos.ProjectDaoDB;
import com.github.kickshare.db.jooq.tables.daos.ProjectPhotoDaoDB;
import com.github.kickshare.db.jooq.tables.daos.TagDaoDB;
import com.github.kickshare.db.jooq.tables.daos.Tag_2CategoryDaoDB;
import com.github.kickshare.db.jooq.tables.daos.TokenRequestDaoDB;
import com.github.kickshare.db.jooq.tables.daos.UsersDaoDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Jan.Kucera
 * @since 10.4.2017
 */
@Configuration
@Import(JooqConfiguration.class)
public class DAOConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(DAOConfiguration.class);

    @Bean
    public ProjectDaoDB projectDao(org.jooq.Configuration configuration) {
        return new ProjectDaoDB(configuration);
    }

    @Bean
    public ProjectPhotoDaoDB projectPhotoDao(org.jooq.Configuration configuration) {
        return new ProjectPhotoDaoDB(configuration);
    }

    @Bean
    public CategoryDaoDB categoryDao(org.jooq.Configuration configuration) {
        return new CategoryDaoDB(configuration);
    }

    @Bean
    public BackerDaoDB backerDao(org.jooq.Configuration configuration) {
        return new BackerDaoDB(configuration);
    }

    @Bean
    public CityDaoDB cityDao(org.jooq.Configuration configuration) {
        return new CityDaoDB(configuration);
    }

    @Bean
    public AddressDaoDB addressDao(org.jooq.Configuration configuration) {
        return new AddressDaoDB(configuration);
    }

    @Bean
    public BackerLocationDaoDB locationsDao(org.jooq.Configuration configuration) {
        return new BackerLocationDaoDB(configuration);
    }

    @Bean
    public Backer_2GroupDaoDB backerToGroupDao(org.jooq.Configuration configuration) {
        return new Backer_2GroupDaoDB(configuration);
    }

    @Bean
    public LeaderDaoDB leaderDao(org.jooq.Configuration configuration) {
        return new LeaderDaoDB(configuration);
    }

    @Bean
    public UsersDaoDB usersDao(org.jooq.Configuration configuration) {
        return new UsersDaoDB(configuration);
    }

    @Bean
    public GroupPostDaoDB groupPostDao(org.jooq.Configuration configuration) {
        return new GroupPostDaoDB(configuration);
    }

    @Bean
    public TagDaoDB tagDao(org.jooq.Configuration configuration) {
        return new TagDaoDB(configuration);
    }

    @Bean
    public Tag_2CategoryDaoDB tag2CategoryDao(org.jooq.Configuration configuration) {
        return new Tag_2CategoryDaoDB(configuration);
    }

    @Bean
    public TokenRequestDaoDB tokenRequestDao(org.jooq.Configuration configuration) {
        return new TokenRequestDaoDB(configuration);
    }

}

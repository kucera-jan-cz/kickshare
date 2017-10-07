package com.github.kickshare.db;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Jan.Kucera
 * @since 3.10.2017
 */
@Configuration
@Import({ DatasourceConfiguration.class, FlywayConfiguration.class, JooqConfiguration.class, DAOConfiguration.class })
public class DatabaseConfiguration {


}

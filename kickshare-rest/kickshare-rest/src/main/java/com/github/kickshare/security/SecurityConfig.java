package com.github.kickshare.security;

import java.util.Arrays;

import javax.sql.DataSource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.kickshare.db.JooqConfiguration;
import com.github.kickshare.db.h2.tables.daos.UserAuthDao;
import com.github.kickshare.db.h2.tables.pojos.UserAuth;
import com.github.kickshare.db.multischema.FlywayMultiTenantMigration;
import com.github.kickshare.db.multischema.MultiSchemaDataSource;
import com.github.kickshare.db.multischema.SchemaContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * @author Jan.Kucera
 * @since 28.3.2017
 */
@Configuration
@Import({ JooqConfiguration.class })
@EnableJdbcHttpSession(maxInactiveIntervalInSeconds = 30)
@ComponentScan(basePackages = { "com.github.kickshare.security.session" })
public class SecurityConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfig.class);
//    @Bean
//    public ObjectMapper objectMapper() {
//        final ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JavaTimeModule());
//        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        return objectMapper;
//    }

    //    @Bean
//    public ObjectMapper objectMapper(BeanFactory beanFactory) {
//        ObjectMapper halObjectMapper = beanFactory.getBean(HAL_OBJECT_MAPPER_BEAN_NAME);
//        //set your flags
//
//        return halObjectMapper
//    }
    //@TODO - figure out usage of this ObjectMapper instead of default one
    @Autowired
    public void configureJackson(@Autowired Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder) {
        jackson2ObjectMapperBuilder.serializationInclusion(JsonInclude.Include.NON_NULL);
        jackson2ObjectMapperBuilder.modulesToInstall(new JavaTimeModule());
    }

//    @Bean
//    public MappingJackson2HttpMessageConverter messageConverter(final ObjectMapper mapper) {
//        final MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(mapper);
//        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON_UTF8));
//        return converter;
//    }

//    @Bean
//    public UserDetailsService userDetailsService() {
//        InMemoryUserDetailsManager service = new InMemoryUserDetailsManager();
//        service.createUser(
//                new User("user", passwordEncoder().encode("user"),
//                        Arrays.asList(new SimpleGrantedAuthority("USER"), new SimpleGrantedAuthority("ACTUATOR")))
//        );
//        service.createUser(
//                new User("admin", "admin",
//                        Arrays.asList(new SimpleGrantedAuthority("USER"), new SimpleGrantedAuthority("ADMIN")))
//        );
//        return service;
//    }

//    @Bean
//    public UserDetailsService userDetailsService(@Autowired org.jooq.Configuration configuration) {
//        return new BackerDetailsService(configuration);
////        return new JdbcUserDetailsManagerConfigurer<>().dataSource(null).withDefaultSchema().passwordEncoder(enc)
////                .withUser("user").password(enc.encode("password")).roles("USER").and()
////                .withUser("admin").password(enc.encode("password")).roles("USER", "ADMIN").and()
////                .getUserDetailsService();
//    }

    @Autowired
    public void setupUsers(PasswordEncoder encoder, org.jooq.Configuration configuration, FlywayMultiTenantMigration migration) {
        //@TODO - get rid of this and move it to data
        LOGGER.info("{}", migration);
        SchemaContextHolder.setSchema("CZ");
        UserAuthDao dao = new UserAuthDao(configuration);
        if (dao.fetchByName("user").isEmpty()) {
            dao.insert(new UserAuth(1L, "user", encoder.encode("user")));
        }
//                .insertInto(USER_AUTH)
//                .columns(USER_AUTH.NAME, USER_AUTH.PASSWORD, USER_AUTH.USER_ID)
//                .values(, 1L)
//                .execute();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
//
//    @Bean
//    public JwtAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
//        return new JwtAuthenticationTokenFilter();
//    }

//    @Bean
//    public JWTSecurityConfig securityConfig() {
//        return new JWTSecurityConfig();//unauthorizedHandler, passwordEncoder, authenticationTokenFilterBean);
//    }

//    @Bean
//    public SessionBasedSecurityConfig securityConfig() {
//        return new SessionBasedSecurityConfig();
//    }

    @Bean
    @Autowired
    public UserDetailsManager udm(DataSource dataSource) {
        ExtendedJdbcUserDetailsManager udm = new ExtendedJdbcUserDetailsManager();
        udm.setDataSource(new MultiSchemaDataSource(dataSource));
        return udm;
    }

    @Bean
    public WebSecurityAdapter securityConfig() {
        return new WebSecurityAdapter();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "OPTION", "HEAD", "PUT"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        return new CookieCsrfTokenRepository();
    }
}

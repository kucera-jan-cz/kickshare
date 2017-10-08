package com.github.kickshare.security;

import com.github.kickshare.db.JooqConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Jan.Kucera
 * @since 28.3.2017
 */
@Configuration

@Import({ JooqConfiguration.class, MethodSecurityConfig.class, CoreSecurityConfig.class, Oauth2ServerConfig.class })
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
//                new Backer("user", passwordEncoder().encode("user"),
//                        Arrays.asList(new SimpleGrantedAuthority("USER"), new SimpleGrantedAuthority("ACTUATOR")))
//        );
//        service.createUser(
//                new Backer("admin", "admin",
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

//    @Bean
//    public WebSecurityAdapter securityConfig() {
//        return new WebSecurityAdapter();
//    }
}

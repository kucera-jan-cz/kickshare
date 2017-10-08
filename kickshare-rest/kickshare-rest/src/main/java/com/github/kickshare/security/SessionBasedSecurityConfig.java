package com.github.kickshare.security;

import com.github.kickshare.security.session.MultiSchemaSessionRepository;
import com.github.kickshare.security.session.RestAuthenticationEntryPoint;
import org.jooq.impl.DataSourceConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.core.serializer.support.DeserializingConverter;
import org.springframework.core.serializer.support.SerializingConverter;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.session.jdbc.JdbcOperationsSessionRepository;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.session.web.http.CookieHttpSessionStrategy;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.session.web.http.HttpSessionStrategy;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author Jan.Kucera
 * @since 14.3.2017
 */
@Configuration
@EnableJdbcHttpSession(maxInactiveIntervalInSeconds = 600)
@ComponentScan(basePackages = { "com.github.kickshare.security.session" })
public class SessionBasedSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CsrfTokenRepository csrfTokenRepository;

    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    private AccessDeniedHandler restAccessDeniedHandler;

    @Autowired
    private AuthenticationSuccessHandler restAuthenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler restAuthenticationFailureHandler;

    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
                .httpBasic()
                .and()
                .sessionManagement()
                .and()
                    .headers().disable()
                    .csrf().disable()
                    .cors()
                .and()
                .authorizeRequests()
                    .antMatchers("/authenticate").anonymous()
                    .antMatchers("/accounts/**").anonymous()
//                    .antMatchers("/groups/search/jsonp").anonymous()
//                    .antMatchers("/cities/search/jsonp").anonymous()
//                    .antMatchers("/groups/search/data.jsonp").anonymous()
//                    .antMatchers("/users/").anonymous()
                    .antMatchers("/admin/**").hasAnyAuthority("admin")
                    .anyRequest().authenticated()
                .and()
                    .exceptionHandling()
                        .authenticationEntryPoint(restAuthenticationEntryPoint)
                        .accessDeniedHandler(restAccessDeniedHandler)
                .and()
                    .formLogin()
                        .loginProcessingUrl("/authenticate")
                        .successHandler(restAuthenticationSuccessHandler)
                        .failureHandler(restAuthenticationFailureHandler)
                        .usernameParameter("username")
                        .passwordParameter("password")
                    .permitAll()
                .and()
                    .logout()
                        .logoutUrl("/logout")
                        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                        .deleteCookies("JSESSIONID")
                    .permitAll();
        // @formatter:on
        http.addFilterBefore(new SchemaHttpFilter(), UsernamePasswordAuthenticationFilter.class);
    }

//    @Bean
//    public WebSecurityAdapter securityConfig() {
//        return new WebSecurityAdapter();
//    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//                .inMemoryAuthentication()
//                .withUser("user").password("user").roles("USER");
        auth
                .userDetailsService(userDetailsService())
                .passwordEncoder(bCryptPasswordEncoder);
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        //Here we probably should place some salt and encryption
        authenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);

        return authenticationProvider;
    }

//    @Bean
//    public UserDetailsService userDetailsService() {
//        InMemoryUserDetailsManager service = new InMemoryUserDetailsManager();
//        service.createUser(new User("user", bCryptPasswordEncoder.encode("user"), Collections.singleton(new SimpleGrantedAuthority("USER"))));
//        return service;
//    }

    @Bean
    @Primary
    public JdbcOperationsSessionRepository sessionRepository(
            @Value("${kickshare.flyway.schemas}") String schemas,
            DataSourceConnectionProvider provider,
            PlatformTransactionManager transactionManager) {
        MultiSchemaSessionRepository sessionRepository = new MultiSchemaSessionRepository(schemas.split(","), provider.dataSource(), transactionManager);
        sessionRepository.setDefaultMaxInactiveInterval(120);

        GenericConversionService conversionService = this.createConversionServiceWithBeanClassLoader();
        sessionRepository.setConversionService(conversionService);

        return sessionRepository;
    }

    @Bean
    public HttpSessionStrategy httpSessionStrategy() {
        CookieHttpSessionStrategy strategy = new CookieHttpSessionStrategy();
        final DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setDomainName("local.kickshare.eu");
        serializer.setUseSecureCookie(true);
        strategy.setCookieSerializer(serializer);
        return strategy;
    }

    private GenericConversionService createConversionServiceWithBeanClassLoader() {
        GenericConversionService conversionService = new GenericConversionService();
        conversionService.addConverter(Object.class, byte[].class, new SerializingConverter());
        conversionService.addConverter(byte[].class, Object.class, new DeserializingConverter());
        return conversionService;
    }
}

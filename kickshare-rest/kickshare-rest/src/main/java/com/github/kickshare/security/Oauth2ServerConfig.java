package com.github.kickshare.security;

import com.github.kickshare.security.oauth2.LogoutSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author Jan.Kucera
 * @since 20.9.2017
 */
@Configuration
@EnableWebSecurity
public class Oauth2ServerConfig extends GlobalAuthenticationConfigurerAdapter {

    private static final String SERVER_RESOURCE_ID = "oauth2-server";

    @Autowired
    private ExtendedJdbcUserDetailsManager service;

    @Autowired
    private PasswordEncoder encoder;


    @Bean
    public TokenStore tokenStore() {
        return new InMemoryTokenStore();
    }

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(service).passwordEncoder(encoder);

//        auth.inMemoryAuthentication()
//                .withUser("xatrix101@gmail.com").password("user").roles("read")
//                .and()
//                .withUser("user").password("password").roles("read");
    }


//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:4200","https://localhost:4200"));
//        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PATCH", "HEAD", "PUT"));
//        configuration.setAllowCredentials(true);
//        configuration.setAllowedHeaders(Arrays.asList("*"));
//        configuration.setMaxAge(TimeUnit.HOURS.toSeconds(1));
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }

    @Bean
    public FilterRegistrationBean corsFilterBean() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }

    //@Serves for registering server
    @Configuration
    @EnableResourceServer
    @ComponentScan(basePackages = { "com.github.kickshare.security.oauth2" })
    protected static class ResourceServer extends ResourceServerConfigurerAdapter {
        @Autowired
        private TokenStore tokenStore;

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
            resources
                    .tokenStore(tokenStore)
                    .resourceId(SERVER_RESOURCE_ID);
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                    .antMatchers(HttpMethod.GET).permitAll()
                    .antMatchers("/login**", "/webjars/**", "/accounts/**").permitAll()
                    .anyRequest().authenticated()
                    .and()
                        .logout()
                        .logoutUrl("/oauth/logout")
                        .logoutSuccessHandler(new LogoutSuccessHandler(tokenStore))
                    .and()
                    .csrf().disable();
            //                    .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            //Register filter
            http.addFilterBefore(new SchemaHttpFilter(), WebAsyncManagerIntegrationFilter.class);
        }

    }

    @Configuration
    @EnableAuthorizationServer
    protected static class AuthConfig extends AuthorizationServerConfigurerAdapter {
        @Autowired
        private TokenStore tokenStore;

        @Autowired
        private AuthenticationManager authenticationManager;

        @Autowired
        private ExtendedJdbcUserDetailsManager service;

        @Override
        public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
//            oauthServer.tokenKeyAccess("permitAll()")
//                    .checkTokenAccess("isAuthenticated()");
            oauthServer.addTokenEndpointAuthenticationFilter(new SchemaHttpFilter());
//            oauthServer.passwordEncoder(encoder);
//            oauthServer.passwordEncoder(encoder);
        }

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            endpoints
                    .authenticationManager(authenticationManager)
                    .tokenStore(tokenStore)
                    .userDetailsService(service)
                    .approvalStoreDisabled();
        }

        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//            clients.jdbc(dataSource).passwordEncoder(encoder);
            clients.inMemory()
                    .withClient("user")
                    .secret("user")
                    .authorizedGrantTypes("password")
                    .scopes("read");
        }
    }
}
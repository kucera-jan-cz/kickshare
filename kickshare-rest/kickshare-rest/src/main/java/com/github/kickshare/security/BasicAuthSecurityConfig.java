package com.github.kickshare.security;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * @author Jan.Kucera
 * @since 14.3.2017
 */
@Component
@AllArgsConstructor
public class BasicAuthSecurityConfig extends WebSecurityConfigurerAdapter {
//        DelegatingWebMvcConfiguration {
    private final MappingJackson2HttpMessageConverter messageConverter;

//    @Override
//    public void configureMessageConverters(final List<HttpMessageConverter<?>> converters) {
//        converters.add(messageConverter);
//    }

    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/system/info").permitAll()
                .antMatchers("/groups/**").permitAll()
                .anyRequest().authenticated();
        http.httpBasic();
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // you USUALLY want this
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("PUT");
        source.registerCorsConfiguration("/**", config);
        http.cors().configurationSource(source);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//                .inMemoryAuthentication()
//                .withUser("user").password("user").roles("USER", "ACTUATOR");
    }


}

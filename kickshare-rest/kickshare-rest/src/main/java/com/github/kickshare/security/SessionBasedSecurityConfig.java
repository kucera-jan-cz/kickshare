package com.github.kickshare.security;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.csrf.CsrfTokenRepository;

/**
 * @author Jan.Kucera
 * @since 14.3.2017
 */
@Configuration
public class SessionBasedSecurityConfig
        extends WebSecurityConfigurerAdapter {

    @Autowired
    CsrfTokenRepository csrfTokenRepository;

    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
//                .csrfTokenRepository(csrfTokenRepository).ignoringAntMatchers()
//                .and()
                .authorizeRequests()
                .antMatchers("/resources/**").permitAll()
                .antMatchers("/sign-up").permitAll()
                .antMatchers("/sign-in").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/")
                .loginProcessingUrl("/loginprocess")
                .failureUrl("/mobile/app/sign-in?loginFailure=true")
                .permitAll()
        ;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//                .inMemoryAuthentication()
//                .withUser("user").password("user").roles("USER");
        auth
                .userDetailsService(userDetailsService())
                .passwordEncoder(bCryptPasswordEncoder());
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        //Here we probably should place some salt and encryption
        authenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());

        return authenticationProvider;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager service = new InMemoryUserDetailsManager();
        service.createUser(new User("user", bCryptPasswordEncoder().encode("user"), Collections.singleton(new SimpleGrantedAuthority("USER"))));
        return service;
    }

}

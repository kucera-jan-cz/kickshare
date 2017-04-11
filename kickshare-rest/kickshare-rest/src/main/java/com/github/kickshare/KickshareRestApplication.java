package com.github.kickshare;

import com.github.kickshare.kickstarter.KSConfiguration;
import com.github.kickshare.mapper.MappingConfiguration;
import com.github.kickshare.security.SecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootConfiguration
@EnableAutoConfiguration(exclude = { })
@ComponentScan(basePackages = { "com.github.kickshare.rest", "com.github.kickshare.service", "com.github.kickshare.db" })
@EnableWebSecurity
@EnableConfigurationProperties
@Import({ SecurityConfig.class, MappingConfiguration.class, KSConfiguration.class })
public class KickshareRestApplication {

//    @Bean
//    public ApplicationSecurityAdapter jwtSecurityConfig() {
//        return new ApplicationSecurityAdapter();
//    }

//    @Bean
//    public JWTSecurityConfig jwtSecurityConfig() {
//        return new JWTSecurityConfig();
//    }
//
//
//    @Bean
//    public JwtAuthenticationEntryPoint authenticationEntryPoint() {
//        return new JwtAuthenticationEntryPoint();
//    }
//
//    @Bean
//    public UserDetailsService userDetailsService() {
//        InMemoryUserDetailsManager service = new InMemoryUserDetailsManager();
//        service.createUser(new User("user", "user", Collections.singleton(new SimpleGrantedAuthority("USER"))));
//        service.createUser(new User("admin", "admin", Collections.singleton(new SimpleGrantedAuthority("ADMIN"))));
//        return service;
//    }
//
//
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public JwtAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
//        return new JwtAuthenticationTokenFilter();
//    }

//    @Bean
//    public SessionBasedSecurityConfig sessionSecurityConfig() {
//        return new SessionBasedSecurityConfig();
//    }

    public static void main(String[] args) {
        SpringApplication.run(KickshareRestApplication.class, args);
    }
}

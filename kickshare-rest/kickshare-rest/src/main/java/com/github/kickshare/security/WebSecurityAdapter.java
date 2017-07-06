package com.github.kickshare.security;

import com.github.kickshare.db.multischema.SchemaContextHolder;
import com.github.kickshare.security.session.RestAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

/**
 * @author Jan.Kucera
 * @since 14.3.2017
 */
@EnableWebSecurity
@Configuration
public class WebSecurityAdapter extends WebSecurityConfigurerAdapter {

    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    private AccessDeniedHandler restAccessDeniedHandler;

    @Autowired
    private AuthenticationSuccessHandler restAuthenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler restAuthenticationFailureHandler;

//    @Autowired
//    private DataSource datasource;

    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }

    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
                .httpBasic()
                .and()
                    .headers().disable()
                    .csrf().disable()
                    .cors()
                .and()
                .authorizeRequests()
                    .antMatchers("/authenticate").anonymous()
                    .antMatchers("/groups//search/jsonp").anonymous()
                    .antMatchers("/cities/search/jsonp").anonymous()
                    .antMatchers("/groups/search/data.jsonp").anonymous()
                    .antMatchers("/users/").anonymous()
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

    @Autowired
    public void setupUsers(PasswordEncoder encoder, UserDetailsManager manager) {
        //@TODO - remove this once close to alpha version
        SchemaContextHolder.setSchema("CZ");
        if (!manager.userExists("xatrix101@gmail.com")) {
            BackerDetails user = new BackerDetails("xatrix101@gmail.com", encoder.encode("user"), 1L, true);
            manager.createUser(user);
        }
    }

//    @Bean
//    public JdbcUserDetailsManager userDetailsManager(@Autowired DataSource dataSource, @Autowired PasswordEncoder encoder) {
//        JdbcUserDetailsManager manager = new JdbcUserDetailsManager();
//        manager.setDataSource(dataSource);
//        manager.setPUsersByUsernameQuery(
//                "select username,password,enabled from users where username=?");
//        manager.setAuthoritiesByUsernameQuery(
//                "select username, role from user_roles where username=?");
//        manager.setRolePrefix("ROLE_");
//        return manager;
//    }

//    @Bean
//    public JdbcUserDetailsManager getUserDetailsManager() {
//        return userDetailsManager;
//    }


    //
//    @Autowired
//    public void configAuthentication(@Autowired AuthenticationManagerBuilder auth) throws Exception {
//        this.userDetailsManager = auth.jdbcAuthentication().passwordEncoder(bCryptPasswordEncoder()).getUserDetailsService();
//        super.configure(auth);
//    }
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth, UserDetailsService uds, PasswordEncoder encoder) throws Exception {
//        auth.jdbcAuthentication().passwordEncoder(new BCryptPasswordEncoder());
        auth
                .userDetailsService(uds)
                .passwordEncoder(encoder);
    }
}

package com.github.kickshare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;

@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(basePackages =  {"com.github.kickshare.rest", "com.github.kickshare.security"})
//@EnableWebMvcSecurity
@EnableWebSecurity
public class KickshareRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(KickshareRestApplication.class, args);
	}
}

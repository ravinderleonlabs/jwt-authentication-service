package com.leonlabs.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@Configuration
//@EnableAsync
@EnableAspectJAutoProxy
@PropertySource({ "classpath:application-authentication.properties", "classpath:messages.properties" })
@ComponentScan(basePackages = "com.leonlabs")
public class AuthenticationMicroservice extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(AuthenticationMicroservice.class, args);
	}

	

}

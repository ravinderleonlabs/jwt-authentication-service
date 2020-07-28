package com.leonlabs.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.leonlabs.auth.security.AuthorizationTokenFilter;
import com.leonlabs.auth.security.JwtAuthenticationEntryPoint;
import com.leonlabs.auth.service.AuthenticationService;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(SecurityProperties.BASIC_AUTH_ORDER)
public class AuthSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Autowired
	AuthorizationTokenFilter authorizationTokenFilter;

	@Autowired
	private AuthenticationService jwtUserDetailsService;

	@Autowired
	PasswordEncoder passwordEncoderBean;

	@Bean
	public PasswordEncoder passwordEncoderBean() {
		return new BCryptPasswordEncoder();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoderBean);
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
				// we don't need CSRF because our token is invulnerable
				.csrf().disable().httpBasic().disable().exceptionHandling()
				.authenticationEntryPoint(jwtAuthenticationEntryPoint).and()

				// don't create session
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
				.antMatchers("/login*", "/master/**", "/signup/**", "/user/create", "/generateToken/**", "/auth/facebook/**",
						"/registration/sendResetPasswordMail", "/registration/confirmRegistration","/upload/downloadFile/**",
						"/registration/resetPassword","/master/profileMasterDataByType", "/search/v1/query/search","/registration/sendResetPasswordMail")
				.permitAll().anyRequest().authenticated();

		httpSecurity.addFilterBefore(authorizationTokenFilter, UsernamePasswordAuthenticationFilter.class);

		// disable page caching
		httpSecurity.headers().frameOptions().sameOrigin().cacheControl();
	}

	@Override
	public void configure(WebSecurity web)  {
		// AuthenticationTokenFilter will ignore the below paths
		web.ignoring().antMatchers(HttpMethod.POST, "/generateToken", "/user/create").and().ignoring().antMatchers(
				HttpMethod.GET, "/", "/*.html", "/favicon.ico", "/**/*.html", "/**/*.css", "/**/*.js", "/master/*");
	}

}

package com.omniwyse.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author aashish.kumar
 */

@Configuration
@EnableWebSecurity
@Order(1)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

	
	@Autowired
	@Qualifier("secureUserDetailsService")
	private UserDetailsService userDetailsService;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests()
			.mvcMatchers("/", "/public/**").permitAll()
			.antMatchers("/aboutus").permitAll()
			.antMatchers("/api/**/register/**", "/api/**/create/**").permitAll()
			.antMatchers("/otp").hasRole("PRE_AUTH_USER")
			//.antMatchers("/dashboard").hasRole("PRE_AUTH_USER")
			.antMatchers("/user/**").access("hasRole('USER') or hasRole('ADMIN') or hasRole('SUPERADMIN') or hasRole('AGENT')")
			.antMatchers("/api/**/agent/**").access("hasRole('SUPERADMIN') or hasRole('ADMIN') or hasRole('AGENT')")
			.antMatchers("/api/**/admin/**").access("hasRole('SUPERADMIN') or hasRole('ADMIN')")
			.antMatchers("/api/**/superadmin/**").hasRole("SUPERADMIN")
			.and()
			.exceptionHandling().accessDeniedPage("/accessDenied")
			.and()
	        .formLogin()
				.loginPage("/login")  //Loginform all can access .. 
				.defaultSuccessUrl("/otp",true)
				.failureUrl("/login?error=true")
				.permitAll()
				.and()
	        .logout()
				.permitAll()
			.and()
			.exceptionHandling().accessDeniedPage("/accessDenied");
	}
	
}

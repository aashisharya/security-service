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

import com.omniwyse.security.handler.CustomSuccessHandler;

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
		/*http.sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
        .and().requestMatchers().antMatchers("/eureka/**")
        .and()
        .authorizeRequests()*/
		http.authorizeRequests()
			//.and().addFilterBefore(new CustomLogoutFilter(), BasicAuthenticationFilter.class)
			//.authorizeRequests()
			.mvcMatchers("/", "/public/**").permitAll()
			.antMatchers("/api/**/register/**", "/api/**/create/**").permitAll()
			.antMatchers("/user/**").access("hasRole('USER') or hasRole('ADMIN') or hasRole('SUPERADMIN') or hasRole('AGENT')")
			.antMatchers("/api/**/agent/**").access("hasRole('SUPERADMIN') or hasRole('ADMIN') or hasRole('AGENT')")
			.antMatchers("/api/**/admin/**").access("hasRole('SUPERADMIN') or hasRole('ADMIN')")
			.antMatchers("/api/**/superadmin/**").hasRole("SUPERADMIN")
			.and()
			.exceptionHandling().accessDeniedPage("/accessDenied")
			.and()
			.formLogin()
			//.successHandler(new CustomSuccessHandler())//.loginPage("/login").failureUrl("/login?error=true") // enable custome form based log in
			//.loginProcessingUrl("/user/login")
			.permitAll();
		
		http.csrf().disable();
			/*.and()
			.logout()//.logoutUrl("/logout")
			.deleteCookies("remember-me", "JSESSIONID")
			.logoutSuccessUrl("/")
			.permitAll()
			.and()
			.rememberMe().tokenRepository(persistentTokenRepository()).tokenValiditySeconds(12000)
			//.rememberMe().tokenRepository(tokenRepository).tokenValiditySeconds(12000)
			.and().sessionManagement().maximumSessions(1);*/
	}
	
	

}

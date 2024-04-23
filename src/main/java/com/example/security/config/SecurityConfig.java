package com.example.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.security.filter.JWTFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	@Autowired
	private JWTFilter filter;
	
	@Bean
	public UserDetailsService userDetailsService() {
//		UserDetails user= User.withUsername("Manoj")
//				.password(encoder.encode("manoj@123"))
//				.roles("USER").build();
//		
//		UserDetails admin= User.withUsername("Bimal")
//				.password(encoder.encode("Bimal@123"))
//				.roles("ADMIN","USER").build();
//		return new InMemoryUserDetailsManager(user,admin);
		
		return new  UserDetailsUserInfoService();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
		
	return	security.csrf().disable()
		.authorizeHttpRequests()
		.requestMatchers("/welcome","/auth").permitAll()
		.and()
		.authorizeHttpRequests().requestMatchers("/get/**","/adduser").authenticated()
		.and()
		.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
		.authenticationProvider(authenticationProvider())
		.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
//		.formLogin().and()
		.build();
		
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider() {
		 DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		 authenticationProvider.setUserDetailsService(userDetailsService());
		 authenticationProvider.setPasswordEncoder(passwordEncoder());
		 
		 return authenticationProvider;
		 
	}
	
	@Bean
	public AuthenticationManager authenticationManager
	(AuthenticationConfiguration configuration) throws Exception {
		
		
	return configuration.getAuthenticationManager();	
	}

}

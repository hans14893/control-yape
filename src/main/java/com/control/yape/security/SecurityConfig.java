package com.control.yape.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    http
	        .csrf(csrf -> csrf.disable())
	        .cors(Customizer.withDefaults())
	        .sessionManagement(sm -> sm.sessionCreationPolicy(
	            org.springframework.security.config.http.SessionCreationPolicy.STATELESS
	        ))
	        .httpBasic(b -> b.disable())
	        .formLogin(f -> f.disable())
	        .authorizeHttpRequests(auth -> auth
	            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
	            .requestMatchers("/ping").permitAll()
	            .requestMatchers("/api/auth/**").permitAll()
	            .anyRequest().authenticated()
	        );

	    http.addFilterBefore(new JwtAuthFilter(), UsernamePasswordAuthenticationFilter.class);
	    return http.build();
	}
}

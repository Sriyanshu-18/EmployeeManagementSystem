package com.ems.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ems.security.JwtAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter) {

        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http

            // Disable CSRF for REST APIs
            .csrf(csrf -> csrf.disable())

            // Stateless session for JWT
            .sessionManagement(session ->
                    session.sessionCreationPolicy(
                            SessionCreationPolicy.STATELESS
                    )
            )

            // Authorization rules
            .authorizeHttpRequests(auth -> auth

            	    // Public frontend files
            	    .requestMatchers(
            	        "/",
            	        "/index.html",
            	        "/css/**",
            	        "/js/**"
            	    ).permitAll()

            	    // Authentication APIs
            	    .requestMatchers(
            	        "/api/auth/**"
            	    ).permitAll()

            	    // Swagger
            	    .requestMatchers(
            	        "/swagger-ui/**",
            	        "/v3/api-docs/**"
            	    ).permitAll()

            	    // Only ADMIN can create employees
            	    .requestMatchers(
            	        org.springframework.http.HttpMethod.POST,
            	        "/api/employees/**"
            	    ).hasRole("ADMIN")

            	    // Only ADMIN can update employees
            	    .requestMatchers(
            	        org.springframework.http.HttpMethod.PUT,
            	        "/api/employees/**"
            	    ).hasRole("ADMIN")

            	    // Only ADMIN can delete employees
            	    .requestMatchers(
            	        org.springframework.http.HttpMethod.DELETE,
            	        "/api/employees/**"
            	    ).hasRole("ADMIN")

            	    // Any authenticated user can view employees
            	    .requestMatchers(
            	        org.springframework.http.HttpMethod.GET,
            	        "/api/employees/**"
            	    ).hasAnyRole("ADMIN", "USER")

            	    .anyRequest().authenticated()
            	)

            // Disable default login form
            .formLogin(form -> form.disable())

            // Disable HTTP Basic authentication
            .httpBasic(basic -> basic.disable())

            // Add JWT filter
            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}
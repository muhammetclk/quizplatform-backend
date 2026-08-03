package com.quizplatform.quizservice.config;

import com.quizplatform.quizservice.security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security configuration for quizservice.
 *
 * Public (no token required):
 *   GET  /api/category/**
 *   GET  /api/topic/**
 *   GET  /api/quiz/**
 *   GET  /actuator/**
 *
 * Admin only (ROLE_ADMIN):
 *   ALL  /api/admin/**
 *
 * Everything else: authenticated (any valid JWT).
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Public read endpoints
                .requestMatchers(HttpMethod.GET,
                        "/api/category",
                        "/api/category/**",
                        "/api/topic/**",
                        "/api/quiz/**"
                ).permitAll()
                // Actuator health
                .requestMatchers("/actuator/**").permitAll()
                // Admin-only write endpoints
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                // Attempt (quiz solving) — any authenticated user
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

package com.example.backendpfe.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final KeycloakRoleConverter keycloakRoleConverter;

    SecurityConfig(KeycloakRoleConverter keycloakRoleConverter) {
        this.keycloakRoleConverter = keycloakRoleConverter;
    }

    @Bean
    @Order(1)
    public SecurityFilterChain publicEndpoints(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/hello", "/about", "/contact", "/auth/**", "/offers/**", "/api/candidates/register","/api/recruiters/register")
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain securedEndpoints(HttpSecurity http) throws Exception {
        http
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS).permitAll()
                        .requestMatchers("/dashboard/admin").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/dashboard/candidate").hasAuthority("ROLE_CANDIDATE")
                        .requestMatchers("/dashboard/recruiter", "/dashboard/recruiter/**").hasAuthority("ROLE_RECRUITER")
                        .requestMatchers("/dashboard/recruiter/offers/**").hasAuthority("ROLE_RECRUITER")
                        /*.requestMatchers("/api/candidates/register").hasAuthority("ROLE_CANDIDATE")
                        .requestMatchers("/api/recruiters/register").hasAuthority("ROLE_RECRUITER")*/
                        .requestMatchers("/api/users").hasAuthority("ROLE_ADMIN")
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(auth -> auth.jwt(jwt -> jwt.jwtAuthenticationConverter(keycloakRoleConverter)));

        return http.build();
    }
}
package com.example.backendpfe.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    KeycloakRoleConverter keycloakRoleConverter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .sessionManagement( session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // Routes publiques
                        .requestMatchers("/", "/about", "/contact").permitAll()
                        .requestMatchers("/auth/sign-in", "/auth/get-started", "/auth/forget-password").permitAll()
                        .requestMatchers("/offers", "/offers/**").permitAll() //consultation des offres publiques

                        // Routes protégées par rôle
                        .requestMatchers("/dashboard/admin").hasRole("ADMIN")
                        .requestMatchers("/dashboard/candidate").hasRole("CANDIDATE")
                        .requestMatchers("/dashboard/recruiter", "/dashboard/recruiter/**").hasRole("RECRUITER") // Inclut la gestion des offres
                        .requestMatchers("/auth/sign-up-candidate").hasRole("CANDIDATE")
                        .requestMatchers("/auth/sign-up-recruiter").hasRole("RECRUITER")

                        // Routes nécessitant une authentification simple
                        .requestMatchers("/profile/**").authenticated()
                        .requestMatchers("/evaluation/**").authenticated()

                        // Toute autre route
                        .anyRequest().authenticated()
                )

        .oauth2ResourceServer(auth->auth.jwt(jwt->
                jwt.jwtAuthenticationConverter(keycloakRoleConverter))); // Active la validation des tokens JWT

        return http.build();
    }
}


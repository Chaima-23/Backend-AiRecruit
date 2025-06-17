package com.example.backendpfe.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
                .securityMatcher("/api/contact", "/offers/**", "/api/candidates/register", "/api/recruiters/register","/api/ollama/hello", "/api/ollama/text")
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain securedEndpoints(HttpSecurity http) throws Exception {
        http
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS).permitAll()
                        // Endpoints pour les recruteurs
                        .requestMatchers(HttpMethod.PUT, "/api/recruiters/**").hasAuthority("ROLE_RECRUITER")
                        .requestMatchers(HttpMethod.GET, "/api/recruiters/current").hasAuthority("ROLE_RECRUITER")
                        // Endpoints pour les candidats
                        .requestMatchers(HttpMethod.PUT, "/api/candidates/**").hasAuthority("ROLE_CANDIDATE")
                        // Endpoints pour les administrateurs
                        .requestMatchers(HttpMethod.DELETE, "/api/recruiters/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/recruiters").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/recruiters/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/candidates/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/candidates").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/candidates/**").hasAuthority("ROLE_ADMIN")
                        // Endpoint pour le dashboard admin
                        .requestMatchers(HttpMethod.GET, "/api/dashboard/stats").hasAuthority("ROLE_ADMIN")
                        // Endpoint pour la gestion des offres par l'administrateur
                        .requestMatchers(HttpMethod.GET, "/dashboard/admin/offers").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET,"/dashboard/admin/offers/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/dashboard/admin/offers/**").hasAuthority("ROLE_ADMIN")
                        // Endpoint pour la consultation des offres par le candidat
                        .requestMatchers(HttpMethod.GET, "/dashboard/candidate/offers").hasAuthority("ROLE_CANDIDATE")
                        .requestMatchers(HttpMethod.GET, "/dashboard/candidate/offers/**").hasAuthority("ROLE_CANDIDATE")
                        // Endpoints pour RecruiterApplicationController et candidatures favoris
                        .requestMatchers(HttpMethod.GET, "/dashboard/recruiter/applications").hasAuthority("ROLE_RECRUITER")
                        .requestMatchers(HttpMethod.GET, "/dashboard/recruiter/applications/**").hasAuthority("ROLE_RECRUITER")
                        .requestMatchers(HttpMethod.POST, "/dashboard/recruiter/applications/*/favorite").hasAuthority("ROLE_RECRUITER")
                        .requestMatchers(HttpMethod.DELETE, "/dashboard/recruiter/applications/*/favorite").hasAuthority("ROLE_RECRUITER")
                        .requestMatchers(HttpMethod.GET, "/dashboard/recruiter/applications/favorites").hasAuthority("ROLE_RECRUITER")
                        // Autres endpoints existants
                        .requestMatchers("/dashboard/candidate").hasAuthority("ROLE_CANDIDATE")
                        .requestMatchers("/dashboard/recruiter", "/dashboard/recruiter/**").hasAuthority("ROLE_RECRUITER")
                        .requestMatchers("/dashboard/recruiter/offers/**").hasAuthority("ROLE_RECRUITER")
                        .requestMatchers("/api/users/admin").hasAuthority("ROLE_ADMIN")
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(auth -> auth.jwt(jwt -> jwt.jwtAuthenticationConverter(keycloakRoleConverter)));

        return http.build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:4200");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
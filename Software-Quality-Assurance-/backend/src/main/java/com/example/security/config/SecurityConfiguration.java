package com.example.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

    // Liste des URLs publiques (pas besoin d'authentification)
    private static final String[] WHITE_LIST_URL = {
            // Authentification
            "/api/v1/auth/**",

            // Pour les tests
            "/test/**",
            "/api/test/**",
            "/debug/**",
            "/api/debug/**",

            // Swagger/OpenAPI
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/webjars/**",
            "/configuration/ui",
            "/configuration/security",

            // H2 Console (si utilisée)
            "/h2-console/**",

            // Actuator
            "/actuator/**",
            "/health",
            "/info",

            // Racine et pages statiques
            "/",
            "/index.html",
            "/favicon.ico",
            "/error",
            "/public/**"
    };

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. DÉSACTIVER CSRF (obligatoire pour API REST)
                .csrf(AbstractHttpConfigurer::disable)

                // 2. CONFIGURATION CORS COMPLÈTE (tous les ports)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 3. CONFIGURATION DES AUTORISATIONS
                .authorizeHttpRequests(req ->
                        req.requestMatchers(WHITE_LIST_URL)
                                .permitAll()        // URLs publiques
                                .anyRequest()
                                .authenticated()    // Le reste nécessite authentification
                )

                // 4. SESSIONS STATELESS (pour JWT)
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))

                // 5. PROVIDER D'AUTHENTIFICATION
                .authenticationProvider(authenticationProvider)

                // 6. FILTRE JWT (s'exécute après les vérifications d'autorisation)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                // 7. LOGOUT
                .logout(logout ->
                        logout.logoutUrl("/api/v1/auth/logout")
                                .addLogoutHandler(logoutHandler)
                                .logoutSuccessHandler((request, response, authentication) ->
                                        SecurityContextHolder.clearContext())
                );

        return http.build();
    }

    /**
     * CONFIGURATION CORS POUR TOUS LES PORTS
     * Autorise localhost sur n'importe quel port
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // ⭐⭐ AUTORISER TOUS LES PORTS DE LOCALHOST ⭐⭐
        configuration.setAllowedOriginPatterns(Arrays.asList(
                // HTTP - tous les ports
                "http://localhost:*",
                "http://127.0.0.1:*",
                "http://0.0.0.0:*",

                // HTTPS - tous les ports
                "https://localhost:*",
                "https://127.0.0.1:*",
                "https://0.0.0.0:*",

                // IPv6
                "http://[::1]:*",
                "https://[::1]:*",

                // Pour les tests réseaux locaux
                "http://192.168.*:*",
                "http://10.*:*",
                "http://172.16.*:*",
                "http://172.17.*:*",
                "http://172.18.*:*",
                "http://172.19.*:*",
                "http://172.20.*:*",
                "http://172.21.*:*",
                "http://172.22.*:*",
                "http://172.23.*:*",
                "http://172.24.*:*",
                "http://172.25.*:*",
                "http://172.26.*:*",
                "http://172.27.*:*",
                "http://172.28.*:*",
                "http://172.29.*:*",
                "http://172.30.*:*",
                "http://172.31.*:*"
        ));

        // Toutes les méthodes HTTP
        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS", "HEAD", "TRACE", "CONNECT"
        ));

        // Tous les headers
        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "X-Requested-With",
                "Accept",
                "Origin",
                "Access-Control-Request-Method",
                "Access-Control-Request-Headers",
                "Cache-Control",
                "Pragma",
                "Expires",
                "If-Match",
                "If-Modified-Since",
                "If-None-Match",
                "If-Unmodified-Since",
                "Range",
                "X-Content-Type-Options",
                "X-Frame-Options",
                "X-XSS-Protection"
        ));

        // Headers exposés dans la réponse
        configuration.setExposedHeaders(Arrays.asList(
                "Access-Control-Allow-Origin",
                "Access-Control-Allow-Credentials",
                "Authorization",
                "Content-Disposition",
                "Content-Length",
                "Content-Type",
                "Date",
                "ETag",
                "Last-Modified",
                "Location",
                "X-Request-ID",
                "X-Total-Count"
        ));

        // Autoriser les credentials (cookies, auth headers)
        configuration.setAllowCredentials(true);

        // Durée de cache pour les préflight requests (2 heures)
        configuration.setMaxAge(7200L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    /**
     * VERSION ALTERNATIVE : Configuration CORS dans application.yml
     * (si vous préférez configurer dans le fichier YAML)
     */
    /*
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        // Spring Boot utilisera automatiquement la configuration
        // définie dans spring.web.cors.* de application.yml
        return new UrlBasedCorsConfigurationSource();
    }
    */
}
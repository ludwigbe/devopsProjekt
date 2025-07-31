package de.THM.devops.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    /**
     * Security configuration for the Spring application.
     *
     * This configuration allows unauthenticated access to the following routes:
     * - /api/**
     * - /
     * - /public/**
     *
     * All other routes require authentication.
     *
     * @param http the HttpSecurity object
     * @return a SecurityFilterChain for the application
     * @throws Exception if any error occurs while building the security filter chain
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configure(http))  // CORS erlauben
                .csrf(AbstractHttpConfigurer::disable)  // CSRF deaktivieren für API-Calls
                .authorizeHttpRequests(authorize -> authorize
                                .requestMatchers("/api/**", "/", "/public/**").permitAll()  // Erlaube Zugriff auf /api/customers ohne Authentifizierung
                                .anyRequest().authenticated()  // Alle anderen Routen benötigen Authentifizierung
               /* )
                .oauth2Login(oauth2Login ->
                        oauth2Login
                                .loginPage("/login") // Custom Login Page if needed
                                .defaultSuccessUrl("/home", true) // Redirect after login
                )
                .logout(logout ->
                        logout
                                .logoutSuccessUrl("/login?logout") // Redirect after logout*/
                );
        return http.build();
    }
}

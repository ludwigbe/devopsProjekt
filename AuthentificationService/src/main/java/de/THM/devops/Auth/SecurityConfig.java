package de.THM.devops.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/**").authenticated()
                .anyRequest().permitAll()
            )
            .oauth2Login(oauth2 -> oauth2
                .successHandler((request, response, authentication) -> {
                    response.sendRedirect("http://localhost:3000/"); // Redirection manuelle vers le frontend
                    System.out.println("Login ist aufgeruft");
                })
                .failureHandler((request, response, exception) -> {
                    response.sendRedirect("/login?error"); // Redirection en cas d'échec
                    System.out.println("Login fehler");
                })
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/api/auth/login?logout") // Redirection après déconnexion
                .invalidateHttpSession(true)
                .clearAuthentication(true)
            );
        return http.build();
    }
}
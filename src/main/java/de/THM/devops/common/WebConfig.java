package de.THM.devops.common;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    /**
     * {@link WebMvcConfigurer} that enables CORS for the whole application.
     *
     * <p>This is necessary because the React frontend is running on a different
     * origin than the Spring Boot backend. This means that the browser will not
     * allow the React application to make requests to the Spring Boot backend
     * unless CORS is enabled.
     *
     * @return The {@link WebMvcConfigurer} instance.
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            /**
             * Configure CORS mappings for the whole application.
             *
             * <p>This method configures CORS mappings for the whole application by
             * adding a mapping for all paths ({@code "/**"}) and allowing requests
             * from the origin {@code "http://localhost:3000"}. This is necessary
             * because the React frontend is running on a different origin than the
             * Spring Boot backend. This means that the browser will not allow the
             * React application to make requests to the Spring Boot backend unless
             * CORS is enabled.
             *
             * @param registry The {@link CorsRegistry} to add the CORS mappings to.
             */
            public void addCorsMappings(@NotNull CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000")
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}

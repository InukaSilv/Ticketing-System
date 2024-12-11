package com.ticketing.system.ticketing_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class to define Cross-Origin Resource Sharing (CORS) settings for the application.
 */
@Configuration
public class WebConfig {

    /**
     * This method sets up allowed origins, methods, headers, and other CORS settings to enable
     * smooth interaction between the frontend and backend.
     *
     * @return a WebMvcConfigurer instance that applies CORS settings.
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            /**
             * Configures the allowed CORS mappings.
             * This method specifies the origins, HTTP methods, headers, and other CORS configurations
             * that allow communication between the backend and frontend.
             *
             * @param registry the CORS registry used to define CORS mappings.
             */
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Allow CORS on all paths
                        .allowedOrigins("http://localhost:5173") // Frontend origin, which allows access
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*") // Allow any headers from the client
                        .allowCredentials(true)
                        .maxAge(3600);
            }
        };
    }
}

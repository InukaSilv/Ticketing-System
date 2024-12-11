package com.ticketing.system.ticketing_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration class for setting up authentication and authorization in the application.
 * It disables CSRF protection and permits all HTTP requests without requiring authentication.
 */
@Configuration
public class SecurityConfig {

    /**
     * This method returns a BCryptPasswordEncoder that will be used for encoding passwords securely.
     *
     * @return the BCryptPasswordEncoder instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Define the password encoder bean
    }

    /**
     * Configures the security settings for HTTP requests.
     * This configuration disables CSRF protection and allows all HTTP requests to be accessed without authentication.
     *
     * @param http HttpSecurity instance used to configure HTTP security.
     * @return the configured SecurityFilterChain.
     * @throws Exception if an error occurs while configuring the security settings.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF protection for this application
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()); // Allow all HTTP requests without authentication

        return http.build(); // Return the configured security filter chain
    }
}

package com.ticketing.system.ticketing_backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Entity representing a user in the ticketing system.
 */
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment ID
    private Long id;

    @Column(nullable = false, unique = true) // Username must be unique and not null
    private String username;

    @Column(nullable = false) // Password must not be null
    private String password;

    @Column(nullable = false) // Required for Spring Security
    private boolean enabled = true; // Defaults to true

    // Getters and Setters

    /**
     * Get the ID of the user.
     * @return the user ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the ID of the user.
     * @param id the user ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get the username of the user.
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the username of the user.
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get the password of the user.
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the password of the user.
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Get the enabled status of the user.
     * @return true if the user is enabled, false otherwise
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Set the enabled status of the user.
     * @param enabled true if the user should be enabled, false otherwise
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}

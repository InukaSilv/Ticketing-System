package com.ticketing.system.ticketing_backend.repository;

import com.ticketing.system.ticketing_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}

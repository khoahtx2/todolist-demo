package com.example.todolist.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.todolist.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}

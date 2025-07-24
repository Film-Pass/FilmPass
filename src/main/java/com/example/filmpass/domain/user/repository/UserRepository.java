package com.example.filmpass.domain.user.repository;

import com.example.filmpass.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    Optional<User> findByEmail(String email);

    Page<User> findAllByDeletedAtIsNullOrderByCreatedAtDesc(Pageable pageable);
}

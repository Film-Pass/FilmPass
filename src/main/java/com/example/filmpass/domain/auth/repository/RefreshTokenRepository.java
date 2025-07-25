package com.example.filmpass.domain.auth.repository;

import com.example.filmpass.domain.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    boolean existsByUser_Id(Long userId);

    void deleteByUser_Id(Long userId);

}

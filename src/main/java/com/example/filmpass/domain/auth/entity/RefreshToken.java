package com.example.filmpass.domain.auth.entity;

import com.example.filmpass.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String refreshToken;
    private LocalDateTime createdAt;
    private LocalDateTime expiredAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;


    public RefreshToken(String refreshToken, LocalDateTime createdAt, LocalDateTime expiredAt, User user) {
        this.refreshToken = refreshToken;
        this.createdAt = createdAt;
        this.expiredAt = expiredAt;
        this.user = user;
    }
}

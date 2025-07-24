package com.example.filmpass.domain.user.entity;

import com.example.filmpass.domain.user.dto.PageResponseDto;
import com.example.filmpass.domain.user.enums.UserRole;
import com.example.filmpass.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "users") // 테이블 이름이 'user'는 예약어일 수 있어서 보통 복수형으로
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 유저 ID (PK)

    @Column(nullable = false, unique = true)
    private String email; // 이메일

    @Column(nullable = false)
    private String password; // 비밀번호

    @Column(nullable = false, unique = true)
    private String nickname; // 닉네임

    private LocalDateTime deletedAt; // 삭제일

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.GUEST;


    public User(String email, String password, String nickname, String name) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.name = name;
    }

    public static PageResponseDto pageToDto(User user) {

        return new PageResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getNickname());

    }

}

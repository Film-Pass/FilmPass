package com.example.filmpass.global.config;

import com.example.filmpass.domain.user.enums.UserRole;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class UserPrincipal implements UserDetails {

    private Long userId;
    private String nickname;
    private UserRole userRole;

    public UserPrincipal(Long userId, String nickname, UserRole userRole) {
        this.userId = userId;
        this.nickname = nickname;
        this.userRole = userRole;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> "ROLE" + userRole.name());
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return nickname;
    }
}

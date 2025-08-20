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
    private boolean isCritic;

    public UserPrincipal(Long userId, String nickname, UserRole userRole, boolean isCritic) {
        this.userId = userId;
        this.nickname = nickname;
        this.userRole = userRole;
        this.isCritic = isCritic;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> "ROLE_" + userRole.name());
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return nickname;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

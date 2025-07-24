package com.example.filmpass.domain.auth.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AuthData {

    private final String email;
    private final String nickname;

}

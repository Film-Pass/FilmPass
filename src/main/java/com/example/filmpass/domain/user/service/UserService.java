package com.example.filmpass.domain.user.service;

import com.example.filmpass.domain.user.dto.UserInfoChangeRequestDto;
import com.example.filmpass.domain.user.dto.UserInfoResponseDto;
import com.example.filmpass.domain.user.dto.PasswordRequestDto;
import com.example.filmpass.domain.user.dto.UserDetailsResponseDto;
import com.example.filmpass.domain.user.entity.User;
import com.example.filmpass.domain.user.enums.UserRole;
import com.example.filmpass.domain.user.repository.UserRepository;
import com.example.filmpass.global.config.UserPrincipal;
import com.example.filmpass.global.exception.CustomException;
import com.example.filmpass.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원 탈퇴 로직
    public void deleteUser(Long id, PasswordRequestDto requestDto) {

        // 유저 조회
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 비밀번호 검증
        if(!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
        }

        // 탈퇴한 회원인지 검증
        if(user.getDeletedAt() != null) {
            throw new CustomException(ErrorCode.DELETED_USER);
        }

        // 소프트 삭제 진행
        user.setDeletedAt(LocalDateTime.now());
        userRepository.save(user);

    }


    // 유저 목록 조회
    public Page<UserInfoResponseDto> getUsers(int page, int size, UserPrincipal principal) {

        // 권한 확인
        if(principal.getUserRole() != UserRole.ADMIN) {
            throw new CustomException(ErrorCode.NOT_ADMIN);
        }

        Pageable pageable = PageRequest.of(page-1, size);

        Page<User> users = userRepository.findAllByOrderByCreatedAtDesc(pageable);

        Page<UserInfoResponseDto> response = users.map(User::pageToDto);

        return response;
    }


    // 유저 단건 조회
    public UserDetailsResponseDto getUser(Long id, UserPrincipal principal) {

        // 권한 확인
        if(principal.getUserRole() != UserRole.ADMIN) {
            throw new CustomException(ErrorCode.NOT_ADMIN);
        }


            User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        UserDetailsResponseDto response = new UserDetailsResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getNickname(),
                user.getDeletedAt(),
                user.getRole().name()
        );

        return response;

    }


    // 내 정보 조회 로직
    public UserInfoResponseDto getMyProfile(PasswordRequestDto requestDto, UserPrincipal principal) {

        User user = userRepository.findById(principal.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 비밀번호 검증 (타인이 내 컴퓨터로 정보 조회하는걸 방지)
        if(!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
        }

        return new UserInfoResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getNickname()
        );

    }


    // 유저 정보 수정 로직
    public UserInfoResponseDto changeUserInfo(
            UserInfoChangeRequestDto request,
            UserPrincipal principal,
            Long id) {

        // 요청받은 id 로 유저 조회
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 로그인한 유저의 id와 변경요청할 유저의 id 를 비교  - 다른 유저의 정보를 수정하려하는지 확인하려는 목적
        if(!user.getId().equals(principal.getUserId())) {
            throw new CustomException(ErrorCode.CHANGE_BLOCKED);
        }

        // 정보수정하기
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setNickname(request.getNickname());

        userRepository.save(user);

        // 응답 생성 및 반환
        return new UserInfoResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getNickname()
        );



    }

}

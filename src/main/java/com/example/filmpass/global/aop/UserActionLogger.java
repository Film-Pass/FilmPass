package com.example.filmpass.global.aop;

import com.example.filmpass.global.config.UserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@Slf4j
public class UserActionLogger {

    @Around("@annotation(trackUserActionAnnotation)")
    public Object logUserAction(ProceedingJoinPoint joinPoint, TrackUserActionAnnotation trackUserActionAnnotation) throws Throwable {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        // 로그에 나타낼 정보
        String actionName = trackUserActionAnnotation.value();
        String userIp = request.getRemoteAddr();
        String methodName = joinPoint.getSignature().toShortString();

        // 인증이 된 사용자가 아니면 익명값 할당
        String userId = "anonymous";
        String nickname = "anonymous";
        String userRole = "unknown";

        // 로그에 나타낼 유저의 정보를 SecurityContextHolder 에서 꺼내서 로그에 나타냄.
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(principal instanceof UserPrincipal userPrincipal) {

            userId = String.valueOf(userPrincipal.getUserId());
            nickname = userPrincipal.getNickname();
            userRole = userPrincipal.getUserRole().name();

        }

        log.warn("[사용자 행동] userId={}, nickname={}, role={}, action={}, method={}, ip={}",
                userId, nickname, userRole, actionName, methodName, userIp);

        return joinPoint.proceed();
    }

}

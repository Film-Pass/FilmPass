package com.example.filmpass.domain.reservation.dto;

public enum SoftDeleteStatus {
    CANCELED, POSSIBLE;

    public static SoftDeleteStatus of(boolean isSoftDelete) {
        return isSoftDelete ? CANCELED : POSSIBLE;
    }
}

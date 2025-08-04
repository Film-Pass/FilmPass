package com.example.filmpass.domain.reservation.dto;

public enum SoftDeleteStatus {
    CANCELED, RESERVED;

    public static SoftDeleteStatus of(boolean isSoftDelete) {
        return isSoftDelete ? CANCELED : RESERVED;
    }
}


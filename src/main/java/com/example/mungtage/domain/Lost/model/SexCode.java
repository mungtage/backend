package com.example.mungtage.domain.Lost.model;

import java.util.Objects;

public enum SexCode {
    M, F, Q;
    public static SexCode of(String sexCode) {
        if (Objects.isNull(sexCode)) {
            return null;
        }
        for (SexCode c : SexCode.values()) {
            if (c.name().equals(sexCode.toUpperCase())) {
                return c;
            }
        }
        throw new RuntimeException("SexCode 에러 발생");
    }
}

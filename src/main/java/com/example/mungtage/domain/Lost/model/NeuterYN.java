package com.example.mungtage.domain.Lost.model;

import java.util.Objects;

public enum NeuterYN {
    Y, N, U;

    public static NeuterYN of(String neuterYn) {
        if (Objects.isNull(neuterYn)) {
            return null;
        }
        for (NeuterYN c : NeuterYN.values()) {
            if (c.name().equals(neuterYn.toUpperCase())) {
                return c;
            }
        }
        throw new RuntimeException("NeuterYN 에러 발생");
    }
}

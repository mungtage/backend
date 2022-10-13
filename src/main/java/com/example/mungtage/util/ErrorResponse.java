package com.example.mungtage.util;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ErrorResponse {

    private List<String> messages;

    public ErrorResponse(List<String> messages) {
        this.messages = messages;
    }
}

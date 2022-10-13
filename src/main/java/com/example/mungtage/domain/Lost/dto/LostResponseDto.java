package com.example.mungtage.domain.Lost.dto;

import com.example.mungtage.domain.Lost.model.Lost;
import com.example.mungtage.domain.Lost.model.NeuterYN;
import com.example.mungtage.domain.Lost.model.SexCode;
import com.example.mungtage.domain.User.model.User;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class LostResponseDto {
    public final Long id;
    public final String animalName;
    public final LocalDate happenDate;
    public final SexCode sexCode;
    public final NeuterYN neuterYN;
    public final Long userId;

    public static LostResponseDto from(Lost lost) {
        return new LostResponseDto(lost.getId(), lost.getAnimalName(), lost.getHappenDate(), lost.getSexCode(), lost.getNeuterYN(), lost.getUser().getId());
    }
}

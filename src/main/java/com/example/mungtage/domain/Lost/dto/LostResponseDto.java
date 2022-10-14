package com.example.mungtage.domain.Lost.dto;

import com.example.mungtage.domain.Lost.model.Lost;
import com.example.mungtage.domain.Lost.model.NeuterYN;
import com.example.mungtage.domain.Lost.model.SexCode;
import lombok.Data;

import java.time.LocalDate;

@Data
public class LostResponseDto {
    public final Long id;
    public final String animalName;
    public final String image;
    public final LocalDate happenDate;
    public final SexCode sexCode;
    public final NeuterYN neuterYN;
    public final Long userId;

    public static LostResponseDto from(Lost lost) {
        return new LostResponseDto(lost.getId(), lost.getAnimalName(), lost.getImage(), lost.getHappenDate(), lost.getSexCode(), lost.getNeuterYN(), lost.getUser().getId());
    }
}

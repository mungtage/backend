package com.example.mungtage.domain.Lost.dto;

import com.example.mungtage.domain.Lost.model.NeuterYN;
import com.example.mungtage.domain.Lost.model.SexCode;
import lombok.Data;

import javax.validation.constraints.NotBlank;


@Data
public class CreateLostRequestDto {
    @NotBlank(message = "animalName 값이 없습니다.")
    public final String animalName;

    @NotBlank(message = "image 값이 없습니다.")
    public final String image;

    @NotBlank(message = "happenDate 값이 없습니다.")
    public final String happenDate;

    @NotBlank(message = "sexCode 값이 없습니다.")
    public final SexCode sexCode;

    @NotBlank(message = "neuterYN 값이 없습니다.")
    public final NeuterYN neuterYN;
}

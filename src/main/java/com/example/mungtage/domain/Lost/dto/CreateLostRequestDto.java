package com.example.mungtage.domain.Lost.dto;

import com.example.mungtage.domain.Lost.model.NeuterYN;
import com.example.mungtage.domain.Lost.model.SexCode;
import lombok.Data;

import javax.validation.constraints.NotBlank;


@Data
public class CreateLostRequestDto {
    @NotBlank
    public final String animalName;

    @NotBlank
    public final String image;

    @NotBlank
    public final String happenDate;

    @NotBlank
    public final SexCode sexCode;

    @NotBlank
    public final NeuterYN neuterYN;
}

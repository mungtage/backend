package com.example.mungtage.domain.Lost.dto;

import com.example.mungtage.domain.Lost.model.NeuterYN;
import com.example.mungtage.domain.Lost.model.SexCode;
import lombok.Data;


@Data
public class CreateLostRequestDto {
    public final String animalName;
    public final String image;
    public final String happenDate;
    public final SexCode sexCode;
    public final NeuterYN neuterYN;
    public final String userId;
}

package com.example.mungtage.domain.Lost.dto;

import com.example.mungtage.domain.Lost.model.NeuterYN;
import com.example.mungtage.domain.Lost.model.SexCode;
import lombok.Data;


@Data
public class CreateLostRequestDto {
    private String animalName;
    private String happen_date;
    private SexCode sexCode;
    private NeuterYN neuterYN;
    private Integer userId;
}

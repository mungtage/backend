package com.example.mungtage.domain.Match.dto;

import com.example.mungtage.domain.Lost.model.Lost;
import com.example.mungtage.domain.Match.Model.MatchResult;
import com.example.mungtage.domain.Match.Model.MatchTrial;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class MatchResponseDto {
    private Long id;
    private Lost lost;
    private List<MatchResult> matchResults;
}

package com.example.mungtage.domain.Match.dto;

import com.example.mungtage.domain.Match.Model.MatchResult;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class MatchResultResponseDto {
    public final Long id;
    public final Long desertionNo;
    public final Integer rank;

    public static MatchResultResponseDto from (MatchResult matchResult) {
        return new MatchResultResponseDto(matchResult.getId(), matchResult.getDesertionNo(), matchResult.getRank());
    }
}

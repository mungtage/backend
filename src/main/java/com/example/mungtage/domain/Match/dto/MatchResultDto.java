package com.example.mungtage.domain.Match.dto;

import com.example.mungtage.domain.Match.model.MatchResult;
import lombok.Data;

@Data
public class MatchResultDto {
    public final Long id;
    public final Long desertionNo;
    public final Integer rank;

    public static MatchResultDto from (MatchResult matchResult) {
        return new MatchResultDto(matchResult.getId(), matchResult.getDesertionNo(), matchResult.getRank());
    }
}

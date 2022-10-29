package com.example.mungtage.domain.Match.dto;

import lombok.Data;

import java.util.List;

@Data
public class MatchResponseDto {
    public final Long lostId;
    public final List<MatchResultWithRescueDto> matchResults;

    public static MatchResponseDto from (
            Long lostId,
            List<MatchResultWithRescueDto> matchResultWithRescueDto
    ) {
        return new MatchResponseDto(
                lostId,
                matchResultWithRescueDto
        );
    }
}

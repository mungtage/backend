package com.example.mungtage.domain.Match.dto;

import lombok.Data;

import java.util.List;

@Data
public class MatchResponseDto {
    public final Long matchTrialId;
    public final Boolean isDone;
    public final Long lostId;
    public final List<MatchResultWithRescueDto> matchResults;

    public static MatchResponseDto from (
            MatchTrialDto matchTrialDto,
            List<MatchResultWithRescueDto> matchResultWithRescueDto
    ) {
        return new MatchResponseDto(
                matchTrialDto.getMatchTrialId(),
                matchTrialDto.getIsDone(),
                matchTrialDto.getLostId(),
                matchResultWithRescueDto
        );
    }
}

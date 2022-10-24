package com.example.mungtage.domain.Match.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class MatchTrialDto {
    private Long matchTrialId;
    private Boolean isDone;
    private Long lostId;
    private List<MatchResultDto> matchResults;
}

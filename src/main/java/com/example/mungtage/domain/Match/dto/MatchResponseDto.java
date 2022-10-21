package com.example.mungtage.domain.Match.dto;

import com.example.mungtage.domain.Match.Model.MatchResult;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class MatchResponseDto {
    private Long matchTrialId;
    private Boolean isDone;
    private Long lostId;
    private List<MatchResultResponseDto> matchResults;
}

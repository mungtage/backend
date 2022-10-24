package com.example.mungtage.domain.Match.dto;

import com.example.mungtage.domain.Rescue.dto.RescueDto;
import lombok.Data;

@Data
public class MatchResultWithRescueDto {
    public final Long id;
    public final Integer rank;
    public final Long desertionNo;
    public final String careNm;
    public final String imageUrl;
    public final String happenDt;
    public final String happenPlace;

    public static MatchResultWithRescueDto from (
            MatchResultDto matchResult,
            RescueDto rescue
    ) {
        return new MatchResultWithRescueDto(
                matchResult.getId(),
                matchResult.getRank(),
                rescue.getDesertionNo(),
                rescue.getCareNm(),
                rescue.getImageUrl(),
                rescue.getHappenDt(),
                rescue.getHappenPlace()
        );
    }
}

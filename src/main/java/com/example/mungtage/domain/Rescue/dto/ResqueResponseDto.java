package com.example.mungtage.domain.Rescue.dto;


import com.example.mungtage.domain.Rescue.Model.Rescue;
import lombok.Data;
import org.joda.time.DateTime;

@Data
public class ResqueResponseDto {
    public final Long desertionNo;
    public final String imageURL;
    public final String happenDt;
    public final String happenPlace;

    public static ResqueResponseDto from (Rescue rescue) {
        return new ResqueResponseDto(
                rescue.getDesertionNo(),
                rescue.getFilename(),
                rescue.getHappenDt(),
                rescue.getHappenPlace()

        );
    }
}

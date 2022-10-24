package com.example.mungtage.domain.Rescue.dto;

import com.example.mungtage.domain.Rescue.model.Rescue;
import lombok.Data;

@Data
public class RescueDto {
    public final Long desertionNo;
    public final String age;
    public final String careAddr;
    public final String careNm;
    public final String careTel;
    public final String imageUrl;
    public final String happenDt;
    public final String happenPlace;
    public final String neuterYn;
    public final String sexCd;
    public final String specialMark;
    public final String kindCd;
    public final String processState;
    public final String weight;

    public static RescueDto from(Rescue rescue) {
        return new RescueDto(
                rescue.getDesertionNo(),
                rescue.getAge(),
                rescue.getCareAddr(),
                rescue.getCareNm(),
                rescue.getCareTel(),
                rescue.getFilename(),
                rescue.getHappenDt(),
                rescue.getHappenPlace(),
                rescue.getNeuterYn(),
                rescue.getSexCd(),
                rescue.getSpecialmark(),
                rescue.getKindCd(),
                rescue.getProcessState(),
                rescue.getWeight()
        );
    }
}

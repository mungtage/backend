package com.example.mungtage.domain.Rescue;

import com.example.mungtage.domain.Rescue.dto.RescueDto;
import com.example.mungtage.domain.Rescue.model.Rescue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class RescueService {
    private final RescueRepository rescueRepository;

    public RescueDto getRescue(Long desertionNo) {
        Rescue rescue = rescueRepository.findById(desertionNo).orElse(new Rescue().builder()
                .desertionNo(desertionNo)
                .age("error")
                .careNm("error")
                .careTel("error")
                .colorCd("error")
                .chargeNm("error")
                .filename("https://muntage.s3.ap-northeast-2.amazonaws.com/static/cc97a4ad-82ed-4ea9-9b0d-ca3aa3d6e24fmungtage-logo.png")
                .happenDt("error")
                .happenPlace("error")
                .kindCd("error")
                .neuterYn("error")
                .noticeComment("error")
                .noticeEdt("error")
                .orgNm("error")
                .noticeNo("error")
                .noticeSdt("error")
                .sexCd("error")
                .popfile("https://muntage.s3.ap-northeast-2.amazonaws.com/static/cc97a4ad-82ed-4ea9-9b0d-ca3aa3d6e24fmungtage-logo.png")
                .officetel("error")
                .processState("error")
                .specialmark("error")
                .weight("error")
                .careAddr("error")
                .build());
        return RescueDto.from(rescue);
    }
}

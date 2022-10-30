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

    public RescueDto getRescue(Long desertionNo) throws EntityNotFoundException {
        Rescue rescue = rescueRepository.findById(desertionNo).orElseThrow(EntityNotFoundException::new);
        return RescueDto.from(rescue);
    }
}

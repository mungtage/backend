package com.example.mungtage.domain.Lost;

import com.example.mungtage.domain.Lost.dto.CreateLostRequestDto;
import com.example.mungtage.domain.Lost.model.Lost;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LostService {
    private LostRepository lostRepository;

    public Lost createLost(CreateLostRequestDto request) {
        Lost lost = new Lost();
        BeanUtils.copyProperties(request, lost);
        return lostRepository.save(lost);
    }

    public List<Lost> getLosts() {
        return lostRepository.findAll();
     }
}

package com.example.mungtage.domain.Lost;

import com.amazonaws.services.kms.model.NotFoundException;
import com.example.mungtage.domain.Lost.dto.CreateLostRequestDto;
import com.example.mungtage.domain.Lost.model.Lost;
import com.example.mungtage.domain.User.UserRepository;
import com.example.mungtage.domain.User.model.User;
import com.example.mungtage.util.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LostService {
    private final LostRepository lostRepository;
    private final UserRepository userRepository;

    @Transactional
    public Lost createLost(CreateLostRequestDto request,String userEmail) {
        System.out.println(request);
        User user = userRepository.findByEmail(userEmail).orElse(null);
        Lost newLost = new Lost(request,user);
        return lostRepository.save(newLost);
    }

    public List<Lost> getLosts(Long userId) {
        return lostRepository.findByUserId(userId);
     }
}

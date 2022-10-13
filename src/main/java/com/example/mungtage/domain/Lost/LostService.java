package com.example.mungtage.domain.Lost;

import com.example.mungtage.domain.Lost.dto.CreateLostRequestDto;
import com.example.mungtage.domain.Lost.model.Lost;
import com.example.mungtage.domain.User.UserRepository;
import com.example.mungtage.domain.User.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LostService {
    private final LostRepository lostRepository;
    private final UserRepository userRepository;

    public Lost createLost(CreateLostRequestDto request) {
        Lost lost = new Lost(request);
        User user = userRepository.getReferenceById(Long.parseLong(request.getUserId()));
        lost.setUser(user);
        return lostRepository.save(lost);
    }

    public List<Lost> getLosts() {
        return lostRepository.findAll();
     }
}

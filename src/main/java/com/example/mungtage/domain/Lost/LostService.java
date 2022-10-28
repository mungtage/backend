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
        User user = userRepository.findByEmail(userEmail).orElseThrow(()-> new BadRequestException("사용자 이름이 없습니다!!"));
        if(lostRepository.countByUserId(user.getId())>=10){
            throw new BadRequestException("실종글을 등록 할수 없습니다.(게시글 10개 이상)");
        }
        Lost newLost = new Lost(request,user);
        return lostRepository.save(newLost);
    }

    public List<Lost> getLosts(Long userId) {
        return lostRepository.findByUserId(userId);
    }

    public String getLostImageURL(Long lostId) {
        Lost lost = lostRepository.findById(lostId).get();
        return lost.getImage();
    }
}

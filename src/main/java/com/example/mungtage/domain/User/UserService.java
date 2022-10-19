package com.example.mungtage.domain.User;

import com.example.mungtage.domain.User.model.User;
import com.example.mungtage.domain.User.dto.CreateUserRequestDto;
import com.example.mungtage.util.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    @Transactional(readOnly = true)
    public User findByUserEmail(final String email){
        if(email.isEmpty()){
            throw new BadRequestException("이메일 값이 없습니다.");
        }
        return userRepository.findByEmail(email).orElse(null);
    }

}

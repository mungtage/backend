package com.example.mungtage.domain.User;

import com.example.mungtage.domain.User.model.User;
import com.example.mungtage.domain.User.dto.CreateUserRequestDto;
import com.example.mungtage.util.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User createUser(CreateUserRequestDto request) {
        if(request.getEmail().isEmpty()){
            throw new BadRequestException("이메일 값이 없습니다.");
        }
        User user= new User(request.getEmail());
        return userRepository.save(user);
    }
}

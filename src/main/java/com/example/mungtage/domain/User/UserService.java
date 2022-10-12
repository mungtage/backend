package com.example.mungtage.domain.User;

import com.example.mungtage.domain.User.model.User;
import com.example.mungtage.domain.User.dto.CreateUserRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private UserRepository userRepository;

    public User createUser(CreateUserRequestDto request) {
        User user = new User();
        user.setEmail(request.getEmail());
        System.out.println(user);
        return userRepository.save(user);
    }
}

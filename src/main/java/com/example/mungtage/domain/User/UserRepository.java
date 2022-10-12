package com.example.mungtage.domain.User;

import com.example.mungtage.domain.User.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}

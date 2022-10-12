package com.example.mungtage.domain.Lost;

import com.example.mungtage.domain.Lost.model.Lost;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LostRepository extends JpaRepository<Lost, Integer> {
}

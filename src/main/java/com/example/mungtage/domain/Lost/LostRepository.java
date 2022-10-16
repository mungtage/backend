package com.example.mungtage.domain.Lost;

import com.example.mungtage.domain.Lost.model.Lost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LostRepository extends JpaRepository<Lost, Long> {
    List<Lost> findByUserId(Long userId);
}

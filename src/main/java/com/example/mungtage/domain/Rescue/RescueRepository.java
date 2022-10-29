package com.example.mungtage.domain.Rescue;

import com.example.mungtage.domain.Rescue.model.Rescue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RescueRepository extends JpaRepository<Rescue, Long> {
    List<Rescue> findByDesertionNo(Long desertionNo);
}

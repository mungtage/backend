package com.example.mungtage.domain.Rescue;

import com.example.mungtage.domain.Rescue.Model.Rescue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RescueRepository extends JpaRepository<Rescue, Long> {
}

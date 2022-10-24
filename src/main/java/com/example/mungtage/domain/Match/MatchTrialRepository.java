package com.example.mungtage.domain.Match;

import com.example.mungtage.domain.Match.model.MatchTrial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchTrialRepository extends JpaRepository<MatchTrial, Long> {
}

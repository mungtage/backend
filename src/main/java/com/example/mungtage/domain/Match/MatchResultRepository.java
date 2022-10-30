package com.example.mungtage.domain.Match;

import com.example.mungtage.domain.Match.model.MatchResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchResultRepository extends JpaRepository<MatchResult, Long> {
    List<MatchResult> findByLostId(Long lostId, Pageable pageable);
    Page<MatchResult> findByLostIdOrderByUpdatedDateDesc(Long lostId, Pageable pageable);
}

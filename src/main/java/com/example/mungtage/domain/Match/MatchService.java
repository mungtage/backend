package com.example.mungtage.domain.Match;

import com.example.mungtage.domain.Lost.LostRepository;
import com.example.mungtage.domain.Lost.model.Lost;
import com.example.mungtage.domain.Match.Model.MatchResult;
import com.example.mungtage.domain.Match.Model.MatchTrial;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatchService {
    private final MatchTrialRepository matchTrialRepository;
    private final MatchResultRepository matchResultRepository;
    private final LostRepository lostRepository;

    public MatchTrial createMatchTrial(Long lostId) {
        MatchTrial matchTrial = new MatchTrial();

        Lost lost = lostRepository.getReferenceById(lostId);

        matchTrial.setLost(lost);
        return matchTrialRepository.save(matchTrial);
    }

    public Boolean createMatchResult(MatchTrial matchTrial, Long desertionNo, int rank) {
        MatchResult matchResult = new MatchResult(desertionNo, rank);
        matchResult.setMatchTrial(matchTrial);
        matchResultRepository.save(matchResult);
        return true;
    }

    public MatchTrial updateMatchTrialDone(Long matchTrialId) {
        MatchTrial matchTrial = matchTrialRepository.findById(matchTrialId).get();
        matchTrial.setIsDone(true);

        return matchTrialRepository.save(matchTrial);
    }
}

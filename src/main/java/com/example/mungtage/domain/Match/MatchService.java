package com.example.mungtage.domain.Match;

import com.example.mungtage.domain.Lost.LostRepository;
import com.example.mungtage.domain.Lost.model.Lost;
import com.example.mungtage.domain.Match.model.MatchResult;
import com.example.mungtage.domain.Match.model.MatchTrial;
import com.example.mungtage.domain.Match.dto.MatchTrialDto;
import com.example.mungtage.domain.Match.dto.MatchResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchService {
    private final MatchTrialRepository matchTrialRepository;
    private final MatchResultRepository matchResultRepository;
    private final LostRepository lostRepository;

    public MatchTrial createMatchTrial(Long lostId) throws ChangeSetPersister.NotFoundException {
        MatchTrial matchTrial = new MatchTrial();

        Lost lost = lostRepository.findById(lostId).orElseThrow(ChangeSetPersister.NotFoundException::new);
        matchTrial.setLost(lost);
        return matchTrialRepository.save(matchTrial);
    }

    public Boolean createMatchResults(MatchTrial matchTrial, List<Long> result) {
        for (int i=0; i<result.size(); i++) {
            MatchResult matchResult = new MatchResult(result.get(i), i+1);
            matchResult.setMatchTrial(matchTrial);
            matchResultRepository.save(matchResult);
        }
        return true;
    }

    @Transactional(readOnly = true)
    public MatchTrialDto updateMatchTrialDone(Long matchTrialId) throws ChangeSetPersister.NotFoundException {
        MatchTrial matchTrial = matchTrialRepository.findById(matchTrialId).orElseThrow(ChangeSetPersister.NotFoundException::new);
        matchTrial.setIsDone(true);

        Lost lost = matchTrial.getLost();
        List<MatchResult> matchResults = matchTrial.getMatchResults();
        List<MatchResultDto> macthResultsResponse = matchResults
                .stream()
                .map(s -> MatchResultDto.from(s))
                .collect(Collectors.toList());

        MatchTrialDto matchTrialDto = new MatchTrialDto();
        matchTrialDto.setMatchTrialId(matchTrial.getId());
        matchTrialDto.setLostId(lost.getId());
        matchTrialDto.setIsDone(matchTrial.getIsDone());
        matchTrialDto.setMatchResults(macthResultsResponse);

        matchTrialRepository.save(matchTrial);

        return matchTrialDto;
    }

    public MatchTrial getMatchTrial(Long matchTrialId) throws ChangeSetPersister.NotFoundException{
        return matchTrialRepository.findById(matchTrialId).orElseThrow(ChangeSetPersister.NotFoundException::new);
    }
}

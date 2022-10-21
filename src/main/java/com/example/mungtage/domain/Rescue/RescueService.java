package com.example.mungtage.domain.Rescue;

import com.example.mungtage.domain.Rescue.Model.Rescue;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RescueService {
    private final RescueRepository rescueRepository;

    public Rescue getRescue(Long desertionNo) throws ChangeSetPersister.NotFoundException {
        return rescueRepository.getReferenceById(desertionNo);
    }
}

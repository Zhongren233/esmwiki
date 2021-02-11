package moe.zr.esmwiki.producer.service.impl;

import moe.zr.entry.WorkCostume;
import moe.zr.esmwiki.producer.repository.WorkCostumeRepository;
import moe.zr.service.WorkCostumeService;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class WorkCostumeServiceImpl implements WorkCostumeService {
    final
    WorkCostumeRepository repository;

    public WorkCostumeServiceImpl(WorkCostumeRepository workCostumeRepository) {
        this.repository = workCostumeRepository;
    }

    public WorkCostume save(WorkCostume workCostume) {
        return repository.save(workCostume);
    }

    public WorkCostume query(WorkCostume workCostume) {
        String id = workCostume.getId();
        if (id == null) throw new IllegalArgumentException();

        Optional<WorkCostume> byId = repository.findById(id);
        if (byId.isPresent()) return byId.get();

        else throw new NoSuchElementException();

    }
}

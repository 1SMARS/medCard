package com.medcard.services.impl;

import com.medcard.entities.History;
import com.medcard.repositories.HistoryRepository;
import com.medcard.services.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoryServiceImpl implements HistoryService {

    private final HistoryRepository historyRepository;

    @Autowired
    public HistoryServiceImpl(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    @Override
    public List<History> getAll() {
        return historyRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        historyRepository.deleteById(id);
    }
}

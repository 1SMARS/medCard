package com.medcard.services;

import com.medcard.entities.History;

import java.util.List;

public interface HistoryService {

    List<History> getAll();

    void delete(Long id);

}

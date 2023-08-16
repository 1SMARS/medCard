package com.medcard.services.impl;

import com.medcard.entities.Doctor;
import com.medcard.entities.Form;
import com.medcard.entities.History;
import com.medcard.repositories.FormRepository;
import com.medcard.repositories.HistoryRepository;
import com.medcard.services.HistoryService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class HistoryServiceImpl implements HistoryService {

    private final HistoryRepository historyRepository;
    private final FormRepository formRepository;


    @Override
    public List<History> getAll(Long patientId) {
        return historyRepository.getAllByPatientId(patientId);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        History history = historyRepository.findById(id).orElseThrow();
        Form form = history.getForm();

        if (form != null) {
            form.setHistory(null);
            if (form.getPatient() != null) {
                form.getPatient().getForms().remove(form);
            }
            formRepository.delete(form);
        }

        historyRepository.deleteById(id);
    }

    @Override
    public void deleteByDoctor(Doctor doctor) {
        historyRepository.deleteByDoctor(doctor);
    }
}

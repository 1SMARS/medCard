package com.medcard.services.impl;

import com.medcard.entities.Form;
import com.medcard.entities.History;
import com.medcard.entities.Patient;
import com.medcard.repositories.FormRepository;
import com.medcard.services.FormService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class FormServiceImpl implements FormService {

    private final FormRepository formRepository;
    private final PatientServiceImpl patientService;

    @Override
    public Form createForm(Long patientId, Form createForm) {

        Patient patient = patientService.getById(patientId);
        Form form = new Form();
        History history = patient.getHistory();
        form.setHistory(history);
        history.setForm(form);

        form.setPatient(patient);
        patient.setForm(form);

        form.setDescription(createForm.getDescription());
        form.setDisease(createForm.getDisease());

        return formRepository.save(form);
    }

    @Override
    public Form updateForm(Long formId, Form updateForm) {
        Form form = formRepository.findById(formId).orElseThrow();

        form.setDisease(updateForm.getDisease());
        form.setDescription(updateForm.getDescription());

        return formRepository.save(form);
    }

    @Override
    public void deleteForm(Long formId) {
        formRepository.deleteById(formId);
    }

    @Override
    public Form getFormByPatientId(Long patientId) {
        return formRepository.findByPatientId(patientId);
    }

    @Override
    public Form getById(Long id) {
        return formRepository.findById(id).orElse(null);

    }
}

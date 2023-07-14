package com.medcard.services.impl;

import com.medcard.dto.form.FormRequest;
import com.medcard.dto.form.FormResponse;
import com.medcard.entities.Form;
import com.medcard.entities.Patient;
import com.medcard.repositories.FormRepository;
import com.medcard.repositories.PatientRepository;
import com.medcard.services.FormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class FormServiceImpl implements FormService {

    private final FormRepository formRepository;
    private final PatientRepository patientRepository;

    @Autowired
    public FormServiceImpl(FormRepository formRepository, PatientRepository patientRepository) {
        this.formRepository = formRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    public FormResponse fillForm(Long patientId, FormRequest formRequest) {
        Patient patient = patientRepository.findById(patientId).orElseThrow(() -> new NoSuchElementException("patient with id: " + patientId + " is not exist!"));

        Form form = new Form();
        form.setPatient(patient);
        patient.setForm(form);
        form.setDescription(formRequest.getDescription());
        form.setDisease(formRequest.getDisease());

        formRepository.save(form);

        return new FormResponse(form.getId(), form.getDisease(), form.getDescription());
    }

    @Override
    public FormResponse updateForm(Long formId, FormRequest formRequest) {
        Form form = formRepository.findById(formId).orElseThrow(() -> new NoSuchElementException("form with id: " + formId + " is not exist!"));
        form.setDisease(formRequest.getDisease());
        form.setDescription(formRequest.getDescription());

        formRepository.save(form);
        return new FormResponse(form.getId(), form.getDisease(), form.getDescription());
    }

    @Override
    public void deleteForm(Long formId) {
        formRepository.deleteById(formId);
    }

    @Override
    public FormResponse getFormByPatientId(Long patientId) {
        Form form = formRepository.findByPatientId(patientId);
        if (form == null) {
            return null;
        }
        return new FormResponse(form.getId(), form.getDisease(), form.getDescription());
    }



    private FormResponse convertToResponse(Form form) {
        FormResponse formResponse = new FormResponse();
        if (form.getId() != null) {
            formResponse.setId(form.getId());
        }
        formResponse.setDescription(form.getDescription());
        formResponse.setDisease(form.getDisease());
        return formResponse;
    }
}

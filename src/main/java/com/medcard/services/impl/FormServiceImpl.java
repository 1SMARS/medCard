package com.medcard.services.impl;

import com.medcard.entities.Doctor;
import com.medcard.entities.Form;
import com.medcard.entities.History;
import com.medcard.entities.Patient;
import com.medcard.repositories.FormRepository;
import com.medcard.services.DoctorService;
import com.medcard.services.FormService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class FormServiceImpl implements FormService {

    private final FormRepository formRepository;
    private final PatientServiceImpl patientService;
    private final DoctorService doctorService;

    @Override
    public Form createForm(Long patientId, Long doctorId, Form createForm) {

        Patient patient = patientService.getById(patientId);
        Doctor doctor = doctorService.getById(doctorId);

        List<History> histories = patient.getHistories();

        // Find the relevant history associated with the specified doctor
        History relevantHistory = null;
        for (History history : histories) {
//            if (history.getDoctorName().equals(doctor.getUser().getFirstname())) {
//                if (history.getDoctorSurname().equals(doctor.getUser().getLastname())) {
//                    if (history.getSpecialization().equals(doctor.getSpecialization())) {
//                        relevantHistory = history;
//                        break;
//                    }
//                }
//            }

            if (history.getDoctor() == doctor) {
                relevantHistory = history;
                        break;
            }
        }

        if (relevantHistory == null) {
            throw new IllegalArgumentException("No relevant history found for the specified doctor.");
        }

        Form form = new Form();
        form.setDescription(createForm.getDescription());
        form.setDisease(createForm.getDisease());

        form.setHistory(relevantHistory);
        relevantHistory.setForm(form);

        form.setPatient(patient);
        form.setDoctor(doctor);

        List<Form> forms = new ArrayList<>(patient.getForms()); // Get the existing forms of the patient
        forms.add(form);
        patient.setForms(forms);

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
    public Form getFormByPatientId(Long patientId, Long doctorId) {
        return formRepository.findByPatientIdAndDoctorId(patientId, doctorId);
    }

//    @Override
//    public Form getById(Long id) {
//        return formRepository.findById(id).orElse(null);
//
//    }
}

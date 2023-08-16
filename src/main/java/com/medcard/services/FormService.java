package com.medcard.services;

import com.medcard.entities.Form;

public interface FormService {

    Form createForm(Long patientId, Long doctorId, Form createForm);

    Form updateForm(Long formId, Form form);

    void deleteForm(Long formId);

    Form getFormByPatientId(Long patientId, Long doctorId);

//    Form getById(Long id);
}

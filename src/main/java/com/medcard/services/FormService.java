package com.medcard.services;

import com.medcard.entities.Form;

public interface FormService {

    Form createForm(Long patientId, Form form);

    Form updateForm(Long formId, Form form);

    void deleteForm(Long formId);

    Form getFormByPatientId(Long patientId);

    Form getById(Long id);
}

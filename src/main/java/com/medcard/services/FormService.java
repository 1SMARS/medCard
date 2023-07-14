package com.medcard.services;

import com.medcard.dto.form.FormRequest;
import com.medcard.dto.form.FormResponse;

public interface FormService {

    FormResponse fillForm(Long patientId, FormRequest formRequest);
    FormResponse updateForm(Long formId, FormRequest formRequest);
    void deleteForm(Long formId);
    FormResponse getFormByPatientId(Long patientId);
}

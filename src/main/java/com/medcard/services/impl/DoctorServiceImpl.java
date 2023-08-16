package com.medcard.services.impl;

import com.medcard.dto.DoctorCreateRequest;
import com.medcard.dto.DoctorUpdateRequest;
import com.medcard.entities.*;
import com.medcard.repositories.*;
import com.medcard.services.DoctorService;
import com.medcard.services.HistoryService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final FormRepository formRepository;
    private final HistoryRepository historyRepository;
    private final AppointmentRepository appointmentRepository;
    private final HistoryService historyService;

    @Override
    public void saveDoctor(DoctorCreateRequest createRequest) {
        User user = new User();
        Doctor doctor = new Doctor();
        doctor.setSpecialization(createRequest.getSpecialization());
        doctorRepository.save(doctor);
        user.setFirstname(createRequest.getFirstname());
        user.setLastname(createRequest.getLastname());
        user.setEmail(createRequest.getEmail());
        user.setPassword(passwordEncoder.encode(createRequest.getPassword()));
        user.setRole(Role.ROLE_DOCTOR);
        user.setDoctor(doctor);
        doctor.setUser(user);
        userRepository.save(user);
    }

    @Override
    public Doctor getById(Long id) {
        return doctorRepository.findById(id).orElseThrow();
    }

    @Override
    public List<Doctor> getAll() {
        return (List<Doctor>) doctorRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        Doctor doctor = doctorRepository.findById(id).orElse(null);

        if (doctor != null) {
            // Delete associations in a proper order to avoid foreign key constraint violations

            // 1. Delete forms associated with patients
            List<Form> forms = new ArrayList<>(doctor.getForms()); // Create a copy to avoid concurrent modification
            for (Form form : forms) {
                // Delete associations with patients
                Patient patient = form.getPatient();
                if (patient != null) {
                    patient.getForms().remove(form);
                    form.setPatient(null);
                }

                if (form.getDoctor() != null) {
                    form.setDoctor(null);
                    doctor.getForms().remove(form);
                }

                // Delete associations with history
                History history = form.getHistory();
                if (history != null) {
                    history.setForm(null);
                }
                formRepository.delete(form);
            }

            // 2. Delete histories associated with the doctor
            List<History> histories = new ArrayList<>(doctor.getHistories()); // Create a copy to avoid concurrent modification
            for (History history : histories) {
                // Delete associations with patients
                Patient patient = history.getPatient();
                if (patient != null) {
                    patient.setHistories(null);
                }

                // Delete associations with form
                Form form = history.getForm();
                if (form != null) {
                    form.setHistory(null);
                }
                historyRepository.delete(history);
            }

            // 3. Delete associations with patients
            List<Patient> patients = new ArrayList<>(doctor.getPatients()); // Create a copy to avoid concurrent modification
            for (Patient patient : patients) {
                patient.getDoctors().remove(doctor);
                patientRepository.save(patient);
            }

            // 4. Delete appointments associated with the doctor
            List<Appointment> appointments = new ArrayList<>(doctor.getAppointments()); // Create a copy to avoid concurrent modification
            for (Appointment appointment : appointments) {
                // Delete associations with patients
                Patient patient = appointment.getPatient();
                if (patient != null) {
                    patient.getAppointments().remove(appointment);
                }
                appointment.setDoctor(null);
                doctor.getAppointments().remove(appointment);
                appointmentRepository.delete(appointment);
            }

            // Finally, delete the doctor
            doctorRepository.deleteById(id);
        }
    }





    @Override
    public Doctor update(Long id, DoctorUpdateRequest updateDoctor, String imageUUID) {
            Doctor doctor = getById(id);
            doctor.getUser().setFirstname(updateDoctor.getFirstname());
            doctor.getUser().setLastname(updateDoctor.getLastname());
            doctor.getUser().setCity(updateDoctor.getCity());
            doctor.getUser().setBirthDate(updateDoctor.getBirthDate());
            doctor.getUser().setPhoneNumber(updateDoctor.getPhoneNumber());
            doctor.setSpecialization(updateDoctor.getSpecialization());
            doctor.getUser().setProfileImg(imageUUID);
            userRepository.save(doctor.getUser());
            doctorRepository.save(doctor);
            return doctor;
    }

    @Override
    public Doctor updateForAdmin(Long id, DoctorUpdateRequest updateDoctor) {
        Doctor doctor = getById(id);
        doctor.getUser().setFirstname(updateDoctor.getFirstname());
        doctor.getUser().setLastname(updateDoctor.getLastname());
        doctor.getUser().setCity(updateDoctor.getCity());
        doctor.getUser().setBirthDate(updateDoctor.getBirthDate());
        doctor.getUser().setPhoneNumber(updateDoctor.getPhoneNumber());
        doctor.setSpecialization(updateDoctor.getSpecialization());
        userRepository.save(doctor.getUser());
        doctorRepository.save(doctor);
        return doctor;
    }
}

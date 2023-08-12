package com.medcard.mapper;

import com.medcard.dto.DoctorDto;
import com.medcard.entities.Doctor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DoctorMapper {

    public DoctorDto convertToDto(Doctor doctor) {
        DoctorDto doctorDto = new DoctorDto();
        doctorDto.setId(doctor.getId());
        doctorDto.setEmail(doctor.getUser().getEmail());
        doctorDto.setFirstname(doctor.getUser().getFirstname());
        doctorDto.setLastname(doctor.getUser().getLastname());
        doctorDto.setCity(doctor.getUser().getCity());
        doctorDto.setBirthDate(doctor.getUser().getBirthDate());
        doctorDto.setPhoneNumber(doctor.getUser().getPhoneNumber());
        doctorDto.setSpecialization(doctor.getSpecialization());
        doctorDto.setProfileImg(doctor.getUser().getProfileImg());
       return doctorDto;
    }

    public List<DoctorDto> convertToDtoList(List<Doctor> doctors) {
        List<DoctorDto> doctorDtoList = new ArrayList<>();

        for (Doctor doctor : doctors) {
            DoctorDto doctorDto = new DoctorDto();
            doctorDto.setId(doctor.getId());
            doctorDto.setEmail(doctor.getUser().getEmail());
            doctorDto.setFirstname(doctor.getUser().getFirstname());
            doctorDto.setLastname(doctor.getUser().getLastname());
            doctorDto.setCity(doctor.getUser().getCity());
            doctorDto.setBirthDate(doctor.getUser().getBirthDate());
            doctorDto.setPhoneNumber(doctor.getUser().getPhoneNumber());
            doctorDto.setSpecialization(doctor.getSpecialization());
            doctorDto.setProfileImg(doctor.getUser().getProfileImg());

            doctorDtoList.add(doctorDto);
        }

        return doctorDtoList;
    }

}

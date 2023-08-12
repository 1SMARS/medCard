package com.medcard.services;

import com.medcard.dto.DoctorCreateRequest;
import com.medcard.dto.DoctorUpdateRequest;
import com.medcard.dto.UserDto;
import com.medcard.entities.Doctor;
import com.medcard.entities.User;

import java.util.List;

public interface UserService {

    void saveUser(UserDto userDto);

    void saveUser(DoctorCreateRequest userDto);

    User findUserByEmail(String email);
}

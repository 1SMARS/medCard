package com.medcard.controllers;

import com.medcard.dto.DoctorCreateRequest;
import com.medcard.dto.UserDto;
import com.medcard.entities.Doctor;
import com.medcard.entities.Patient;
import com.medcard.entities.Role;
import com.medcard.entities.User;
import com.medcard.repositories.PatientRepository;
import com.medcard.services.DoctorService;
import com.medcard.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class SecurityController {

    private final UserService userService;
    private final DoctorService doctorService;
    private final PatientRepository patientRepository;

    @GetMapping("/login")
    public String login(){
        return "security/login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model){
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "security/register";
    }

    @PostMapping("/register/save")
    public String registration(@ModelAttribute("user") UserDto userDto, BindingResult result, Model model){
        User existingUser = userService.findUserByEmail(userDto.getEmail());

        if(existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()){
            result.rejectValue("email", null,
                    "There is already an account registered with the same email");
        }

        if(result.hasErrors()){
            model.addAttribute("user", userDto);
            return "/register";
        }

        userService.saveUser(userDto);
        return "redirect:/register?success";
    }
}

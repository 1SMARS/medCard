package com.medcard.dto;

import lombok.Data;

@Data
public class UserDto {
    private String email;
    private String password;
    private String firstname;
    private String lastname;
}

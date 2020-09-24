package com.example.cattery.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class PasswordDTO {

    private String newPassword;

    private  String token;

    private String matchingPassword;
}

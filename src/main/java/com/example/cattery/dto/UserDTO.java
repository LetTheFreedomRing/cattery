package com.example.cattery.dto;

import com.example.cattery.validator.PasswordMatches;
import com.example.cattery.validator.ValidEmail;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@PasswordMatches
public class UserDTO {
    @NotEmpty
    @NotNull
    private String name;
    @NotEmpty
    @NotNull
    @ValidEmail
    private String email;
    @NotEmpty
    @NotNull
    private String password;
    private String matchingPassword;
}

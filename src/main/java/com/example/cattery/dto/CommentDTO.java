package com.example.cattery.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
public class CommentDTO {

    private Long id;
    @NotNull
    @NotEmpty
    private String message;
    private UserDTO user;
    private CatDTO cat;
    private LocalDate date;
}

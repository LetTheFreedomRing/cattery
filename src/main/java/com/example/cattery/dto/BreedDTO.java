package com.example.cattery.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@EqualsAndHashCode
public class BreedDTO {
    private Long id;

    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    @NotEmpty
    private String overview;

    @NotNull
    @NotEmpty
    private String history;

    @NotNull
    @NotEmpty
    private String temper;

    @NotNull
    @NotEmpty
    private String care;

    private Byte[] image;
}

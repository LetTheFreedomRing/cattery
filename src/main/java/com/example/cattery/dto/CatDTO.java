package com.example.cattery.dto;

import com.example.cattery.model.CatClass;
import com.example.cattery.model.CatStatus;
import com.example.cattery.model.Gender;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class CatDTO {
    private Long id;
    @NotNull
    @NotEmpty
    private String name;
    @NotNull
    private Gender gender;
    @NotNull
    @NotEmpty
    private String colour;
    @NotNull
    @NotEmpty
    private String ems;
    @NotNull
    @Max(10000)
    @Min(1)
    private Integer price;
    @NotNull
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate birthDate;
    @NotNull
    private CatStatus status;
    @NotNull
    private CatClass catClass;
    private BreedDTO breed;
    private List<Byte[]> images = new ArrayList<>();
}

package com.example.cattery.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "cats")
public class Cat extends BaseEntity {
    private String name;
    private Gender gender;
    private String colour;
    private String ems;
    @Column(name="birth_date")
    private LocalDate birthDate;
    private CatStatus status;
    @Column(name="cat_class")
    private CatClass catClass;
    private String breed;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;

}

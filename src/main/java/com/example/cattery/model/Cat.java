package com.example.cattery.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
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
    private Integer price;
    @Column(name="birth_date")
    private LocalDate birthDate;
    private CatStatus status;
    @Column(name="cat_class")
    private CatClass catClass;
    @ManyToOne
    private Breed breed;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;
    @Column(name = "last_updated")
    private LocalDate lastUpdated;

}

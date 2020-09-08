package com.example.cattery.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@EqualsAndHashCode(exclude = {"owner", "comments"}, callSuper = false)
public class Cat extends BaseEntity {
    private String name;
    private Gender gender;
    private String colour;
    private String ems;
    private Integer price;
    @Column(name="birth_date")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate birthDate;
    private CatStatus status;
    @Column(name="cat_class")
    private CatClass catClass;
    @ManyToOne
    private Breed breed;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cat")
    private List<Comment> comments = new ArrayList<>();
    @Column(name = "last_updated")
    private LocalDate lastUpdated;
    @ElementCollection
    @Lob
    private List<Byte[]> images = new ArrayList<>();

}

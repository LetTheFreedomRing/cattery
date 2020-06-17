package com.example.cattery.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "breed")
public class Breed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Lob
    private String description;
    //todo : add image
    @OneToMany(mappedBy = "breed")
    private Set<Cat> cats = new HashSet<>();

    public Set<Cat> getNCats(Integer n) {
        if (cats.size() <= n) return cats;
        return cats.stream().limit(n).collect(Collectors.toSet());
    }
}

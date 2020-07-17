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
    @Lob
    private Byte[] image;
    @OneToMany(mappedBy = "breed")
    private Set<Cat> cats = new HashSet<>();

    public Set<Cat> getAvailableCats(Integer limit) {
        if (cats.size() <= limit) return cats;
        return cats.stream().filter(cat -> cat.getStatus().equals(CatStatus.AVAILABLE)).limit(limit).collect(Collectors.toSet());
    }
}

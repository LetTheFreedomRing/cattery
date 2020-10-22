package com.example.cattery.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@EqualsAndHashCode(exclude = {"owner", "comments", "isWishedBy"}, callSuper = false)
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cat")
    private List<Comment> comments = new ArrayList<>();
    @Column(name = "last_updated")
    private LocalDate lastUpdated;
    @ElementCollection
    @Lob
    private List<Byte[]> images = new ArrayList<>();
    @ManyToMany(mappedBy = "wishList")
    private Set<User> isWishedBy = new HashSet<>();

    @PreRemove
    private void removeFromUsersWishlist() {
        for (User user : isWishedBy) {
            user.getWishList().remove(this);
        }
    }

}

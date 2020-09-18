package com.example.cattery.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User extends BaseEntity {

    private String name;
    private String password;
    private String email;
    @Column(name="registration_date")
    private LocalDate registrationDate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
    private List<Cat> cats = new ArrayList<>();
    @Column(name="wish_list")
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "cats_users_wish_list",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "cat_id"))
    private Set<Cat> wishList = new HashSet<>();
    @Column(name = "enabled")
    private boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles = new HashSet<>();

    public User() {
        super();
        this.enabled = false;
    }
}

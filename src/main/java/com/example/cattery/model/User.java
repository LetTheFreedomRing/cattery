package com.example.cattery.model;

import com.example.cattery.validator.PasswordMatches;
import com.example.cattery.validator.ValidEmail;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@PasswordMatches
@Table(name = "users")
public class User extends BaseEntity {
    @NotNull
    @NotEmpty
    private String name;
    @NotNull
    @NotEmpty
    private String password;
    private String matchingPassword;
    @NotNull
    @NotEmpty
    @ValidEmail
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
    private List<Cat> wishList = new ArrayList<>();
}

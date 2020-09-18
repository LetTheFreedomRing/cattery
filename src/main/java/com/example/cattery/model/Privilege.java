package com.example.cattery.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.Set;

@Entity
@EqualsAndHashCode(callSuper = false, exclude = {"roles"})
@Getter
@Setter
@ToString
public class Privilege extends BaseEntity {
    private String name;

    @ManyToMany(mappedBy = "privileges")
    private Set<Role> roles;
}

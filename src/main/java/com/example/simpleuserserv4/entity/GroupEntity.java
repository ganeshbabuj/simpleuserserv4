package com.example.simpleuserserv4.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

// group is reserved word. using _group instead
@Table(name = "_group")
@Getter
@Setter
@Entity
public class GroupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "groups")
    private Set<UserEntity> users = new HashSet<>();


}

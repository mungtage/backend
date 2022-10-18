package com.example.mungtage.domain.User.model;

import com.example.mungtage.domain.Lost.model.Lost;
import com.example.mungtage.util.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "user")
@SQLDelete(sql = "UPDATE lost SET deleted = true where id = ?")
@Where(clause = "is_deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;


    @Column()
    private Boolean isDeleted;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Role role;

    @JsonBackReference
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Lost> losts= new ArrayList<>();

    @Builder
    public User(String name, String email, Role role){
        this.name = name;
        this.email = email;
        this.role = role;
        this.isDeleted=false;
    }

    public User update(String name) {
        this.name=name;
        return this;
    }

    public String getRoleKey(){
        return this.role.getKey();
    }
}

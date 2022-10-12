package com.example.mungtage.domain.Lost.model;

import com.example.mungtage.domain.User.model.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "lost")
@SQLDelete(sql = "UPDATE lost SET deleted = true where id = ?")
@Where(clause = "deleted = false")
public class Lost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String animalName;

    @DateTimeFormat()
    private LocalDateTime happen_date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SexCode sexCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NeuterYN neuterYN;

    @Column(columnDefinition = "boolean default false")
    private Boolean isMatched;

    @CreationTimestamp()
    private LocalDateTime createdAt;

    @UpdateTimestamp()
    private LocalDateTime updatedAt;

    @Column(columnDefinition = "boolean default false")
    private Boolean isDeleted = Boolean.FALSE;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonManagedReference
    private User user;
}

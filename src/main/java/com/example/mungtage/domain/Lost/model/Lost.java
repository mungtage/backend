package com.example.mungtage.domain.Lost.model;

import com.example.mungtage.domain.Lost.dto.CreateLostRequestDto;
import com.example.mungtage.domain.Match.Model.MatchTrial;
import com.example.mungtage.domain.User.model.User;
import com.example.mungtage.util.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "lost")
@SQLDelete(sql = "UPDATE lost SET is_deleted = true where id = ?")
@Where(clause = "is_deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Lost extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String animalName;

    private String image;

    @DateTimeFormat()
    private LocalDate happenDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SexCode sexCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NeuterYN neuterYN;

    @Column(columnDefinition = "boolean default false")
    private Boolean isMatched = Boolean.FALSE;

    @Column(columnDefinition = "boolean default false")
    private Boolean isDeleted = Boolean.FALSE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonManagedReference
    private User user;

    @OneToMany(mappedBy = "lost", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonBackReference
    private List<MatchTrial> matchTrials = new ArrayList<>();

    public Lost (CreateLostRequestDto request) {
        this.isDeleted = false;
        this.animalName = request.getAnimalName();
        this.image = request.getImage();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.happenDate = LocalDate.parse(request.getHappenDate(), formatter);
        this.sexCode = request.getSexCode();
        this.neuterYN = request.getNeuterYN();
    }
}

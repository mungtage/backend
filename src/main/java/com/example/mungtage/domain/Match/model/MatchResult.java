package com.example.mungtage.domain.Match.model;

import com.example.mungtage.domain.Lost.model.Lost;
import com.example.mungtage.util.BaseEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "match_result")
@SQLDelete(sql = "UPDATE match_result SET is_deleted = true where id = ?")
@Where(clause = "is_deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchResult extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lost_id")
    @JsonManagedReference
    private Lost lost;

    @Column(nullable = false)
    private Long desertionNo;

    @Column(nullable = false)
    private Integer rank;

    @Column(columnDefinition = "boolean default false")
    private Boolean isDeleted = Boolean.FALSE;

    public MatchResult (Long desertionNo, int rank) {
        this.desertionNo = desertionNo;
        this.rank = rank;
    }
}

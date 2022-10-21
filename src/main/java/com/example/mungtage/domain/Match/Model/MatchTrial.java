package com.example.mungtage.domain.Match.Model;

import com.example.mungtage.domain.Lost.model.Lost;
import com.example.mungtage.util.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Proxy;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "match_trial")
@SQLDelete(sql = "UPDATE match_trial SET is_deleted = true where id = ?")
@Where(clause = "is_deleted = false")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class MatchTrial extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "boolean default false")
    private Boolean isDone = Boolean.FALSE;

    @Column(columnDefinition = "boolean default false")
    private Boolean isDeleted = Boolean.FALSE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lost_id")
    @JsonManagedReference
    private Lost lost;

    @OneToMany(mappedBy = "matchTrial", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonBackReference
    private List<MatchResult> matchResults = new ArrayList<>();
}

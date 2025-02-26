package org.spring.attraction.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "attractiontype")
@Getter
@Setter
public class AttractionType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attractionTypeId")
    private Long id;

    @Column(length = 45 ,nullable = false)
    private String type;

    @OneToMany(mappedBy = "attractionType")
    private Set<AttractionTypeList> attractionTypeListSet;
}

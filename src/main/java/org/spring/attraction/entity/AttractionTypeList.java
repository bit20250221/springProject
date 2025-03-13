package org.spring.attraction.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.spring.attraction.dto.AttractionTypeListDto;

@Entity
@Table(name = "attractiontypelist")
@Getter
@Setter
public class AttractionTypeList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attractionId", nullable = false)
    private Attraction attraction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attractionTypeId", nullable = false)
    private AttractionType attractionType;

}

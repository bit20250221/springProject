package org.spring.attraction.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.spring.attraction.dto.AttractionTypeDto;

import java.time.LocalDateTime;
import java.util.HashSet;
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

    @OneToMany(mappedBy = "attractionType", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AttractionTypeList> attractionTypeListSet = new HashSet<>();

    public static AttractionType toEntity(AttractionTypeDto attractionTypeDto) {
        AttractionType attractionType = new AttractionType();
        attractionType.setId(attractionTypeDto.getId());
        attractionType.setType(attractionTypeDto.getType());
        return attractionType;
    }
}
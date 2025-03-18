package org.spring.attraction.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "attractionimg")
@Getter
@Setter
public class AttractionImg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attractionimgId")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name="uuid", nullable = false)
    private String UUID;

    @OneToOne
    @JoinColumn(name = "attractionId", nullable = false)
    private Attraction attraction;

}

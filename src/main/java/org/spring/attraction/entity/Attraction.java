package org.spring.attraction.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "attraction")
@Getter
@Setter
public class Attraction{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attractionId")
    private Long id;

    @Column(length = 45, nullable = false)
    private String name;

    @ColumnDefault("0")
    @Column(nullable = false)
    private double avgRate;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private LocalDateTime openTime;

    @Column(nullable = false)
    private LocalDateTime closeTime;

    @ColumnDefault("''")
    @Column(length = 500)
    private String explanation;

    @ManyToOne
    @JoinColumn(name = "areaId", nullable = false)
    private Area area;

    @OneToMany(mappedBy = "attraction")
    private Set<AttractionTypeList> attractionsTypeLists;

    @OneToMany(mappedBy = "attraction")
    private Set<Board> Boards;

    @OneToOne(mappedBy = "attraction")
    private User user;

    @OneToMany(mappedBy = "attraction")
    private Set<Reservation> reservations;
}

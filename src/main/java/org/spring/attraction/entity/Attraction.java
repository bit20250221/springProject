package org.spring.attraction.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.spring.attraction.dto.AttractionDto;
import java.time.LocalTime;
import java.util.HashSet;
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

    private LocalTime opentime;

    @Column(nullable = false)
    private LocalTime closetime;

    @ColumnDefault("''")
    @Column(length = 500)
    private String explanation;

    @ManyToOne
    @JoinColumn(name = "areaId", nullable = false)
    private Area area;

    @OneToMany(mappedBy = "attraction", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AttractionTypeList> attractionsTypeLists = new HashSet<>();

    @OneToMany(mappedBy = "attraction", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Board> Boards = new HashSet<>();

    @OneToOne(mappedBy = "attraction")
    private User user;

    @OneToMany(mappedBy = "attraction", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Reservation> reservations = new HashSet<>();

    @OneToOne(mappedBy = "attraction", cascade = CascadeType.ALL, orphanRemoval = true)
    private AttractionImg attractionImg;

    public static Attraction toAttractionEntity(AttractionDto attractionDto) {
        Attraction attraction = new Attraction();
        attraction.setId(attractionDto.getId());
        attraction.setName(attractionDto.getName());
        attraction.setPrice(attractionDto.getPrice());
        attraction.setOpentime(attractionDto.getOpenTime());
        attraction.setClosetime(attractionDto.getCloseTime());
        attraction.setAvgrate(attractionDto.getAvgrate());
        attraction.setExplanation(attractionDto.getExplanation());
        return attraction;
    }


}

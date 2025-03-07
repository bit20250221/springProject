package org.spring.attraction.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.spring.attraction.dto.AreaDto;

import java.util.Set;

@Entity
@Table(name = "area")
@Getter
@Setter
public class Area {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "areaId")
    private Long id;

    @Column(length = 45, nullable = false)
    private String country;

    @Column(length = 45, nullable = false)
    private String city;

    @OneToMany(mappedBy = "area")
    private Set<Attraction> attractions;

    public static Area toAreaEntity(AreaDto areaDto){
        Area area = new Area();
        area.setId(areaDto.getId());
        area.setCountry(areaDto.getCountry());
        area.setCity(areaDto.getCity());
        return area;
    }
}
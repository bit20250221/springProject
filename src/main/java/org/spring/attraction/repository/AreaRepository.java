package org.spring.attraction.repository;

import org.spring.attraction.entity.Area;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AreaRepository extends JpaRepository<Area, Long> {
    List<Area> findByCountryAndCity(String country, String city);

}

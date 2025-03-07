package org.spring.attraction.repository;

import org.spring.attraction.entity.Attraction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttractionRepository extends JpaRepository<Attraction, Long> {
    List<Attraction> findByAreaId(Long id);
}

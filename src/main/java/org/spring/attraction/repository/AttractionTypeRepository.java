package org.spring.attraction.repository;

import org.spring.attraction.entity.Attraction;
import org.spring.attraction.entity.AttractionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttractionTypeRepository extends JpaRepository<AttractionType, Long> {
}

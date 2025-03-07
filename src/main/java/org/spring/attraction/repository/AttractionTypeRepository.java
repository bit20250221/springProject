package org.spring.attraction.repository;

import org.spring.attraction.entity.Attraction;
import org.spring.attraction.entity.AttractionType;
import org.spring.attraction.entity.AttractionTypeList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttractionTypeRepository extends JpaRepository<AttractionType, Long> {
    AttractionType findByType(String type);
}

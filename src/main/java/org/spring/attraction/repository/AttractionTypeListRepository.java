package org.spring.attraction.repository;

import org.spring.attraction.entity.Attraction;
import org.spring.attraction.entity.AttractionType;
import org.spring.attraction.entity.AttractionTypeList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttractionTypeListRepository extends JpaRepository<AttractionTypeList, Long> {
    List<AttractionTypeList> findByAttractionId(Long id);

    AttractionTypeList findByAttractionIdAndAttractionTypeId(Long id, Long id1);

    List<AttractionTypeList> findByAttractionTypeId(Long id);
}

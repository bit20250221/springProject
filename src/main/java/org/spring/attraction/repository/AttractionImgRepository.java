package org.spring.attraction.repository;

import org.spring.attraction.entity.AttractionImg;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttractionImgRepository extends JpaRepository<AttractionImg, Long> {
    AttractionImg findByAttractionId(Long id);
}

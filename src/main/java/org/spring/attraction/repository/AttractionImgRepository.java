package org.spring.attraction.repository;

import org.spring.attraction.entity.AttractionImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AttractionImgRepository extends JpaRepository<AttractionImg, Long> {
    Optional<AttractionImg> findByAttractionId(Long id);
}

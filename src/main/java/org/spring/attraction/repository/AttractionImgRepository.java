package org.spring.attraction.repository;

import org.spring.attraction.entity.AttractionImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AttractionImgRepository extends JpaRepository<AttractionImg, Long> {
    @Query(value = "SELECT a FROM AttractionImg a WHERE a.attraction.id = :id")
    Optional<AttractionImg> findByAttractionId(@Param("id") Long id);
}

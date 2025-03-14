package org.spring.attraction.repository;

import org.spring.attraction.entity.ViewAttraction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ViewAttractionRepository extends JpaRepository<ViewAttraction, Integer> {
    Page<ViewAttraction> findByNameContaining(String search, Pageable pageable);

    Page<ViewAttraction> findByTypeContaining(String search, Pageable pageable);

    Page<ViewAttraction> findByAreaContaining(String search, Pageable pageable);
}

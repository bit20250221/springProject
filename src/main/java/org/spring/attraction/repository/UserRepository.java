package org.spring.attraction.repository;

import org.spring.attraction.entity.Attraction;
import org.spring.attraction.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserLoginId(String userLoginId);

    boolean existsByUserLoginId(String userLoginId);

    User findByAttraction(Attraction attraction);
}

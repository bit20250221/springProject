package org.spring.attraction.repository;

import org.spring.attraction.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface User_repository extends JpaRepository<User,Long> {

}

package org.spring.attraction.repository;

import org.spring.attraction.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // 로그인 아이디로 회원 조회
    Optional<User> findByUserLoginId(String userLoginId);

    // 중복된 회원이 있는지 검증
    boolean existsByUserLoginId(String userLoginId);
}

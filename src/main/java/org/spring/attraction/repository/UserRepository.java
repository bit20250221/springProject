package org.spring.attraction.repository;

import org.spring.attraction.dto.user.ViewUserDTO;
import org.spring.attraction.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // 로그인 아이디로 회원 조회
    Optional<User> findByUserLoginId(String userLoginId);

    // 중복된 회원이 있는지 검증
    boolean existsByUserLoginId(String userLoginId);
    
    // DTO로 모든 회원 조회 (비밀번호 제외)
    @Query("SELECT new org.spring.attraction.dto.user.ViewUserDTO(u.id, u.userLoginId, u.birthDate, u.userType, u.grade, u.attraction.id) from User u")
    List<ViewUserDTO> findAllWithoutPassword();
}

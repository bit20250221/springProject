package org.spring.attraction.service;

import org.spring.attraction.dto.user.UserDTO;
import org.spring.attraction.entity.User;
import org.spring.attraction.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    @Autowired
    private UserRepository userRepository;

    // 전체 유저 조회
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(UserDTO::fromUser).toList();
    }

    // 유저 정보 업데이트
    public UserDTO updateUser(UserDTO userDTO) {
        User existingUser = userRepository.findById(userDTO.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 비밀번호는 기존 값 유지
        if (userDTO.getPass() == null || userDTO.getPass().isEmpty()) {
            userDTO.setPass(existingUser.getPass());
        }

        // 업데이트할 필드들을 설정
        existingUser.setUserLoginId(userDTO.getUserLoginId());
        existingUser.setUserType(userDTO.getUserType());
        existingUser.setBirthDate(userDTO.getBirthDate());
        existingUser.setGrade(userDTO.getGrade());

        userRepository.save(existingUser);
        return userDTO;
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public Page<UserDTO> paging(Pageable pageable) {
        int page = pageable.getPageNumber() - 1;
        int pageLimit = 20; // 한 페이지에 보여줄 개수
        Page<User> user = userRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC,"id")));

        Page<UserDTO> userDTOPage = user.map(userEntity -> new UserDTO(
                userEntity.getId(),
                userEntity.getUserLoginId(),
                userEntity.getBirthDate(),
                userEntity.getUserType(),
                userEntity.getGrade(),
                userEntity.getAttraction() != null ? userEntity.getAttraction().getId() : null))
                ;
        return userDTOPage;
    }
}

package org.spring.attraction.service;

import org.spring.attraction.ENUM.UserType;
import org.spring.attraction.dto.UserDto;
import org.spring.attraction.entity.Attraction;
import org.spring.attraction.entity.User;
import org.spring.attraction.repository.AttractionRepository;
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

    @Autowired
    private UserService userService;

    @Autowired
    private AttractionRepository attractionRepository;

    // 관리자 추가
    public void createUser(UserDto userDto, UserType userType) {
        // UserDto에 UserType 설정
        userDto.setUserType(userType);

        // UserService의 registerProcess 호출
        userService.registerProcess(userDto);
    }

    // 전체 유저 조회
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(UserDto::fromUser).toList();
    }

    // 유저 정보 업데이트
    public UserDto updateUser(UserDto userDto) {
        User existingUser = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 비밀번호는 기존 값 유지
        if (userDto.getPass() == null || userDto.getPass().isEmpty()) {
            userDto.setPass(existingUser.getPass());
        }

        // 업데이트할 필드 설정
        existingUser.setUserLoginId(userDto.getUserLoginId());
        existingUser.setUserType(userDto.getUserType());
        existingUser.setBirthDate(userDto.getBirthDate());
        existingUser.setGrade(userDto.getGrade());

        // Attraction 설정 추가
        if (userDto.getAttraction() != null) {
            Attraction attraction = attractionRepository.findById(userDto.getAttraction())
                    .orElseThrow(() -> new RuntimeException("Attraction not found"));
            existingUser.setAttraction(attraction); // Attraction 매핑
        } else {
            existingUser.setAttraction(null); // Attraction 초기화
        }

        userRepository.save(existingUser); // 변경 사항 저장
        return userDto;
    }


    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public Page<UserDto> paging(Pageable pageable) {
        int page = pageable.getPageNumber() - 1;
        int pageLimit = 20; // 한 페이지에 보여줄 개수
        Page<User> user = userRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC,"id")));

        Page<UserDto> userDtoPage = user.map(userEntity -> new UserDto(
                userEntity.getId(),
                userEntity.getUserLoginId(),
                userEntity.getBirthDate(),
                userEntity.getUserType(),
                userEntity.getGrade(),
                userEntity.getAttraction() != null ? userEntity.getAttraction().getId() : null));
        return userDtoPage;
    }
}

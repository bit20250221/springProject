package org.spring.attraction.dto.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.spring.attraction.ENUM.Grade;
import org.spring.attraction.ENUM.UserType;
import org.spring.attraction.entity.User;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String userLoginId;
    private String pass;
    private LocalDate birthDate;
    private UserType userType;
    private Grade grade;
    private Long attraction;

    public UserDTO(Long id, String userLoginId, LocalDate birthDate, UserType userType, Grade grade, Long attraction) {
        this.id = id;
        this.userLoginId = userLoginId;
        this.birthDate = birthDate;
        this.userType = userType;
        this.grade = grade;
        this.attraction = attraction;
    }

    public static UserDTO fromUser(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUserLoginId(user.getUserLoginId());
        userDTO.setUserType(user.getUserType());
        userDTO.setPass(user.getPass());
        userDTO.setBirthDate(user.getBirthDate());
        userDTO.setGrade(user.getGrade());
        userDTO.setAttraction(user.getAttraction() != null ? user.getAttraction().getId() : null);
        return userDTO;
    }
}

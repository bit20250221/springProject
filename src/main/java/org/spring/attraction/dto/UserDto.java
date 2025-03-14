package org.spring.attraction.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.spring.attraction.ENUM.Grade;
import org.spring.attraction.ENUM.UserMessage;
import org.spring.attraction.ENUM.UserType;
import org.spring.attraction.entity.User;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@ToString
public class UserDto {
    private Long id;
    private String userLoginId;
    private String pass;
    private LocalDate birthDate;
    private UserType userType;
    private Grade grade;
    private Long attraction;

    public static UserDto fromUser(User user) {
        UserDto UserDto = new UserDto();
        UserDto.setId(user.getId());
        UserDto.setUserLoginId(user.getUserLoginId());
        UserDto.setUserType(user.getUserType());
        UserDto.setPass(user.getPass());
        UserDto.setBirthDate(user.getBirthDate());
        UserDto.setGrade(user.getGrade());
        UserDto.setAttraction(user.getAttraction() != null ? user.getAttraction().getId() : null);
        return UserDto;
    }

    public static UserMessage validate(UserDto UserDto) {
        String userLoginId = UserDto.getUserLoginId().trim();
        if(userLoginId.isEmpty()) {
            return UserMessage.getTypeById(-1);
        }else if(userLoginId.length() > 10) {
            return UserMessage.getTypeById(-2);
        }

        String password = UserDto.getPass().trim();
        if(password.isEmpty()) {
            return UserMessage.getTypeById(-3);
        }else if(password.length() < 10 || password.length() > 30) {
            return UserMessage.getTypeById(-4);
        }

        return null;
    }
}

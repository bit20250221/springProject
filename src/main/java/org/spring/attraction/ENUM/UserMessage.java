package org.spring.attraction.ENUM;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserMessage {
    SAVE_SUCCESS(1, "성공적으로 저장되었습니다."),
    UPDATE_SUCCESS(2, "성공적으로 삭제되었습니다."),
    USER_LOGIN_ID_IS_EMPTY(-1, "ID가 입력되지 않았습니다."),
    USER_LOGIN_ID_SIZE_IS_WRONG(-2, "ID는 1~10자로 입력이 가능합니다."),
    PASSWORD_IS_EMPTY(-3, "비밀번호가 입력되지 않았습니다."),
    PASSWORD_SIZE_IS_WRONG(-4, "비밀번호는 10~30자로 입력이 가능합니다."),
    USER_LOGIN_ID_ALREADY_EXISTS(-5, "중복된 ID는 입력이 불가능합니다.");

    private final int id;
    private final String message;

    public static UserMessage getTypeById(int id) {
        for (UserMessage type : UserMessage.values()) {
            if (type.getId() == id) {
                return type;
            }
        }
        throw new IllegalArgumentException("해당 인덱스의 관광지 타입이 없습니다: " + id);
    }
}

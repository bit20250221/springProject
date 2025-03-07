package org.spring.attraction.ENUM;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AttractionTypeMessage {
    SAVE_SUCCESS(1, "성공적으로 저장되었습니다."),
    DELETE_SUCCESS(2, "성공적으로 삭제가 되었습니다."),
    DUPLICATE_INPUT(-1, "구분은 중복해서 입력할 수 없습니다."),
    INFORMATION_IN_USE(-2, "사용중인 관광지 구분은 삭제하실 수 없습니다.");

    private final int id;
    private final String message;

    public static String getMessageById(int id) {
        for (AttractionTypeMessage type : AttractionTypeMessage.values()) {
            if (type.getId() == id) {
                return type.getMessage();
            }
        }
        throw new IllegalArgumentException("해당 인덱스의 관광지 타입이 없습니다: " + id);
    }
}

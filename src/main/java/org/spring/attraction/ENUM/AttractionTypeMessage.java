package org.spring.attraction.ENUM;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AttractionTypeMessage {
    SAVE_SUCCESS(1, "성공적으로 저장되었습니다."),
    DELETE_SUCCESS(2, "성공적으로 삭제가 되었습니다."),
    DUPLICATE_INPUT(-1, "구분은 중복해서 입력할 수 없습니다."),
    INFORMATION_IN_USE(-2, "사용중인 관광지 구분은 삭제하실 수 없습니다."),
    TYPE_IS_EMPTY(-3, "구분이 입력되지 않았습니다."),
    TYPE_SIZE_IS_WRONG(-4, "구분은 1~10자로 입력이 가능합니다.");


    private final int id;
    private final String message;

    public static AttractionTypeMessage getTypeById(int id) {
        for (AttractionTypeMessage type : AttractionTypeMessage.values()) {
            if (type.getId() == id) {
                return type;
            }
        }
        throw new IllegalArgumentException("해당 인덱스의 관광지 타입이 없습니다: " + id);
    }
}

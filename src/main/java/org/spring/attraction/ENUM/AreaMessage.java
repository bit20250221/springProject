package org.spring.attraction.ENUM;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AreaMessage {
    SAVE_SUCCESS(1, "성공적으로 저장되었습니다."),
    UPDATE_SUCCESS(2, "성공적으로 삭제되었습니다."),
    DUPLICATE_INPUT(-1, "지역은 중복해서 입력할 수 없습니다."),
    INFORMATION_IN_USE(-2, "사용중인 지역은 삭제하실 수 없습니다."),
    COUNTRY_IS_EMPTY(-3, "국가가 입력되지 않았습니다."),
    COUNTRY_SIZE_IS_WRONG(-4, "국가는 1~10자로 입력이 가능합니다."),
    CITY_IS_EMPTY(-5, "도시가 입력되지 않았습니다."),
    CITY_SIZE_IS_WRONG(-6, "도시는 1~10자로 입력이 가능합니다.");



    private final int id;
    private final String message;

    public static AreaMessage getTypeById(int id) {
        for (AreaMessage type : AreaMessage.values()) {
            if (type.getId() == id) {
                return type;
            }
        }
        throw new IllegalArgumentException("해당 인덱스의 관광지 타입이 없습니다: " + id);
    }
}

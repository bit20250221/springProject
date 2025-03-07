package org.spring.attraction.ENUM;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AttractionMessage {
    SAVE_SUCCESS(1, "성공적으로 저장되었습니다."),
    UPDATE_SUCCESS(2, "성공적으로 수정되었습니다."),
    AREA_NOT_FOUND(-1, "지역 정보를 불러오지 못 했습니다."),
    ATTRACTION_TYPE_NOT_FOUND(-2, "관광지 구분 정보를 불러오지 못 했습니다."),
    ATTRACTION_NOT_FOUND(-3, "관광지 정보를 불러오지 못 했습니다."),
    INFORMATION_IN_USE(-4, "예약된 정보가 있어 삭제할 수 없습니다."),
    NAME_IS_EMPTY(-5, "이름이 입력되지 않았습니다."),
    NAME_SIZE_IS_WRONG(-6, "이름은 1~10자로 입력이 가능합니다."),
    PRICE_TYPE_IS_WRONG(-7, "가격은 음수를 입력할 수 없습니다."),
    PRICE_SIZE_IS_WRONG(-8, "가격은 1000~500000범위 내에서 입력이 가능합니다."),
    TYPE_IS_EMPTY(-9, "관광지 구분은 최소 1개 이상 선택하셔야 합니다."),
    TYPE_SIZE_IS_WRONG(-10, "관광지 구분은 최대 3개까지 선택이 가능합니다.");

    private final int id;
    private final String message;

    public static AttractionMessage getTypeById(int id) {
        for (AttractionMessage type : AttractionMessage.values()) {
            if (type.getId() == id) {
                return type;
            }
        }
        throw new IllegalArgumentException("해당 인덱스의 관광지 타입이 없습니다: " + id);
    }
}

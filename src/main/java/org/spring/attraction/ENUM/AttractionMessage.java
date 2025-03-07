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
    INFORMATION_IN_USE(-4, "예약된 정보가 있어 삭제할 수 없습니다.");

    private final int id;
    private final String message;

    public static String getMessageById(int id) {
        for (AttractionMessage type : AttractionMessage.values()) {
            if (type.getId() == id) {
                return type.getMessage();
            }
        }
        throw new IllegalArgumentException("해당 인덱스의 관광지 타입이 없습니다: " + id);
    }
}

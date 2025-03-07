package org.spring.attraction.ENUM;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReservationMessage {
    SAVE_SUCCESS(1, "성공적으로 저장되었습니다."),
    UPDATE_SUCCESS(2, "성공적으로 수정되었습니다."),
    DELETE_SUCCESS(3, "성공적으로 삭제가 되었습니다."),
    USER_NOT_FOUND(-1, "유저 정보를 불러오지 못 했습니다."),
    ATTRACTION_NOT_FOUND(-2, "관광지 정보를 불러오지 못 했습니다."),
    PAYMENT_TYPE_NOT_FOUND(-3, "결제방법 정보를 불러오지 못 했습니다."),
    RESERVATION_NOT_FOUND(-4, "예약정보를 불러오지 못 했습니다.");


    private final int id;
    private final String message;

    public static String getMessageById(int id) {
        for (ReservationMessage type : ReservationMessage.values()) {
            if (type.getId() == id) {
                return type.getMessage();
            }
        }
        throw new IllegalArgumentException("해당 인덱스의 관광지 타입이 없습니다: " + id);
    }
}

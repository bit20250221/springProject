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
    RESERVATION_NOT_FOUND(-4, "예약정보를 불러오지 못 했습니다."),
    PEPLENUM_IS_EMPTY(-5, "인원수가 입력되지 않았습니다."),
    PEPLENUM_TYPE_IS_WRONG(-6, "인원수는 음수를 입력할 수 없습니다."),
    PEPLENUM_SIZE_IS_WRONG(-7, "인원수는 1~10명까지 입력이 가능합니다."),
    RESERVEDATE_IS_EMPTY(-8, "예약일자가 입력되지 않았습니다."),
    RESERVEDATE_IS_PAST(-9, "예약일자는 과거의 날짜로는 입력하실 수 없습니다."),
    RESERVEDATE_IS_TOO_FAR(-10, "예약일자는 최대 60일 이내까지 입력이 가능합니다."),
    PAY_TYPE_IS_EMPTY(-11, "결제방법이 입력되지 않았습니다."),
    CONVERSION_NOT_POSSIBLE(-12, "타입이 맞지 않아 형변환이 불가능합니다.");


    private final int id;
    private final String message;

    public static ReservationMessage getTypeById(int id) {
        for (ReservationMessage type : ReservationMessage.values()) {
            if (type.getId() == id) {
                return type;
            }
        }
        throw new IllegalArgumentException("해당 인덱스의 관광지 타입이 없습니다: " + id);
    }
}

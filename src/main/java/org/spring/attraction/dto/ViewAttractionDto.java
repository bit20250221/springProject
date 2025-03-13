package org.spring.attraction.dto;


import lombok.*;
import java.time.LocalTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ViewAttractionDto {
    private Long id;
    private String name;
    private double avgrate;
    private int price;
    private LocalTime openTime;
    private LocalTime closeTime;
    private String type;
    private String area;
}

package org.spring.attraction.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import java.time.LocalTime;

@Entity
@Table(name = "viewattraction")
@Getter
@Setter
@Immutable
public class ViewAttraction {
    @Id
    @Column
    private Long id;

    @Column
    private String name;

    @Column
    private double avgrate;

    @Column
    private int price;

    @Column
    private LocalTime openTime;

    @Column
    private LocalTime closeTime;

    @Column
    private String type;

    @Column
    private String area;
}

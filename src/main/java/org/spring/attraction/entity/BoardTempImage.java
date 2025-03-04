package org.spring.attraction.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "boardtempimage")
@Getter
@Setter
public class BoardTempImage {
    //아이디, 이름, 크기, 저장 경로

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "boardTempImageId")
    private Long id;

    @Column(length = 45, nullable = false)
    private String name;

    @Column
    private int ImageSize;

    @Column(length = 500, nullable = false)
    private String ImagePath;
}

package org.spring.attraction.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "boardimage")
@Getter
@Setter
public class BoardImage {
    //아이디, 이름, 크기, 게시글 아이디, 저장 경로

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "boardImageId")
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;

    @Column
    private int ImageSize;

    @Column(length = 500, nullable = false)
    private String ImagePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boardId", nullable = true)
    private Board board;

    @Column
    private Boolean isTemporary;
}

package org.spring.attraction.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Formula;
import org.spring.attraction.ENUM.Tab;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "board")
@Getter
@Setter
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "boardId")
    private Long id;

    @Column(length = 45, nullable = false)
    private String title;

    @Column(length = 100, nullable = false)
    private String content;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Tab tab;

    @Column
    private int rate;

    @Column()
    private LocalDateTime updateDate;

    @ManyToOne
    @JoinColumn(name="userId", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name="attractionId")
    private Attraction attraction;

    @OneToMany(mappedBy = "board")
    private List<Comment> comments;

    @OneToMany(mappedBy = "board")
    private List<BoardImage> boardImages;

    @Formula("(select count(*) from comment c where c.board_id = board_id)")
    private int commentCount;
}

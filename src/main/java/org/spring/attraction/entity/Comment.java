package org.spring.attraction.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
@Getter
@Setter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdate;

    @Column(length = 300, nullable = false)
    private String content;

    @Column(updatable = true)
    private LocalDateTime updatedate;

    @ManyToOne
    @JoinColumn(name="userId", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name="boardId", nullable = false)
    private Board board;

}

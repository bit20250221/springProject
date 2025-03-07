package org.spring.attraction.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.spring.attraction.ENUM.Grade;
import org.spring.attraction.ENUM.UserType;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "user")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private Long id;

    @Column(name = "user_login_Id", length = 45, nullable = false, unique = true, insertable = false, updatable = false)
    private String user_login_Id;

    @Column(length = 45, nullable = false)
    private String pass;

    @Column(nullable = false)
    private Date birthdate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserType userType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @ColumnDefault("'bronze'")
    private Grade grade;

    @OneToMany(mappedBy = "user")
    private Set<Board> boards;

    @OneToMany(mappedBy = "user")
    private Set<Comment> comments;

    @OneToMany(mappedBy =  "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Reservation> reservations;

    @OneToOne
    @JoinColumn(name = "attractionId", nullable = true)
    private Attraction attraction;

}
package org.spring.attraction.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.spring.attraction.ENUM.Grade;
import org.spring.attraction.ENUM.UserType;
import org.hibernate.annotations.ColumnDefault;
import org.spring.attraction.dto.UserDto;

import java.time.LocalDate;
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

    @Column(name = "user_login_id", length = 45, nullable = false, unique = true, updatable = false)

    private String userLoginId;

    @Column(nullable = false)
    private String pass;

    @Column(nullable = false)

    private LocalDate birthDate;

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

    public static User toEntity(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setUserLoginId(userDto.getUserLoginId());
        user.setPass(userDto.getPass());
        user.setBirthDate(userDto.getBirthDate());
        user.setUserType(UserType.nomal);
        user.setGrade(Grade.bronze);
        return user;
    }
}
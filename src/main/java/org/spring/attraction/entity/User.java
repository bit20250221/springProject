package org.spring.attraction.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.spring.attraction.ENUM.Grade;
import org.spring.attraction.ENUM.UserType;
import org.hibernate.annotations.ColumnDefault;
import org.spring.attraction.dto.user.UserDTO;

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

    @Column(length = 45, nullable = false, unique = true)
    private String userLoginId;

    @Column(length = 255, nullable = false)
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

    @OneToMany(mappedBy =  "user")
    private Set<Reservation> reservations;

    @OneToOne
    @JoinColumn(name = "attractionId", nullable = true)
    private Attraction attraction;

    public static User toUser(UserDTO userDTO, String existingPass) {
        User user = new User();
        user.setUserLoginId(userDTO.getUserLoginId());
        user.setPass(userDTO.getPass() != null ? userDTO.getPass() : existingPass);        user.setBirthDate(userDTO.getBirthDate());
        user.setUserType(userDTO.getUserType());
        user.setGrade(userDTO.getGrade());
        user.setAttraction(userDTO.getAttraction() != null ? user.getAttraction() : null);
        return user;
    }
}
package org.spring.attraction.service;

import org.spring.attraction.ENUM.Grade;
import org.spring.attraction.ENUM.UserType;
import org.spring.attraction.dto.Board_dto;
import org.spring.attraction.entity.User;
import org.spring.attraction.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

@Component
public class BoardAndCommentDataInitialize implements CommandLineRunner {

    @Autowired
    private Board_service boardService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        //initSetting();
        //initSetting2();
    }

    //초기 유저 생성 메소드
    public void initSetting(){
        Calendar birthCalender=Calendar.getInstance();
        birthCalender.set(1995,Calendar.JUNE,1);
        Date birthDate=birthCalender.getTime();

        User user=new User();
        user.setUser_login_Id("testuser");
        user.setPass("1111");
        user.setBirthdate(birthDate);
        user.setGrade(Grade.bronze);
        user.setUserType(UserType.nomal);

        userRepository.save(user);
    }

    //임의의 게시글 1000개 데이터 삽입 메소드
    public void initSetting2(){
        for(int i=0;i<1000;i++){
            Board_dto insert=new Board_dto();
            insert.setTitle("test "+i);
            insert.setContent("this is test "+i);
            insert.setTab("일반");
            insert.setCreartedate(LocalDateTime.now());
            insert.setUser_id(1L);
            boardService.writeBoard(insert);
        }
    }
}

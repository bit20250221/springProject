package org.spring.attraction.BoardAndCommentTest;


import org.junit.jupiter.api.Test;
import org.spring.attraction.service.Board_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BoardTest {

    @Autowired
    private Board_service boardService;

    @Test
    public void boardTest(){

    }
}

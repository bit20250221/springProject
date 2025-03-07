package org.spring.attraction.BoardAndCommentTest;


import org.junit.jupiter.api.Test;
import org.spring.attraction.ENUM.Grade;
import org.spring.attraction.ENUM.UserType;
import org.spring.attraction.dto.Board_dto;
import org.spring.attraction.entity.User;
import org.spring.attraction.repository.Board_repository;
import org.spring.attraction.repository.UserRepository;
import org.spring.attraction.service.Board_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

@SpringBootTest
public class BoardTest {

    @Autowired
    private Board_service boardService;
    @Autowired
    private Board_repository boardRepository;
    @Autowired
    private UserRepository userRepository;


    @Test
    public void boardTest(){
        initSetting();
        initSetting2();
        //resetAll();

        //아래부터는 테스트 영역
        //testSearch("","title","test 1",0,5);
        //testRead(1L);
        //testUpdate(1L,"수정된 title","수정된 content","리뷰",1L,4);
        //testDelete(1L);
        testReadAll();

    }

    //게시글 전체 읽어오기 테스트 메소드
    public void testReadAll(){
        HashMap<Long, Board_dto> boardlist=boardService.getBoardList();
        for(Long i:boardlist.keySet()){
            Board_dto element=boardlist.get(i);
            printBoardInfo(element);
        }
    }

    //게시글 삭제 테스트 메소드
    public void testDelete(Long id){
        boardService.deleteBoard(id);
        Board_dto deleteBoard=boardService.getBoard(id);
        if(deleteBoard==null){
            System.out.println("정상 삭제 되었습니다.");
        }
    }
    //게시글 수정 테스트 메소드
    public void testUpdate(Long id, String reTitle, String reContent, String reTab, Long reAttraction_id, int reRate){
        Board_dto update=boardService.getBoard(id);

        update.setTitle(reTitle);
        update.setContent(reContent);
        update.setTab(reTab);
        update.setRate(reRate);
        //update.setAttraction_id(reAttraction_id);
        boardService.updateBoard(update);

        Board_dto updateInfo=boardService.getBoard(id);
        printBoardInfo(updateInfo);
    }

    //게시글 읽기 테스트 메소드
    public void testRead(Long id){
        Board_dto read=boardService.getBoard(id);
        printBoardInfo(read);
    }

    //게시글 검색 테스트 메소드
    public void testSearch(String InTab, String InType, String InKeyword, int InPageNum, int InPageAmount){

        String tab=InTab;
        String type=InType;
        String keyword=InKeyword;
        Page<Board_dto> pageResult=boardService.getSearchPageBoard(tab,type,keyword,InPageNum,InPageAmount);

        System.out.println("검색 조건(탭): "+tab);
        System.out.println("검색 조건(타입): "+type);
        System.out.println("검색 조건(키워드): "+keyword);
        System.out.println("전체 건수: " + pageResult.getTotalElements());
        System.out.println("전체 페이지: " + pageResult.getTotalPages());
        System.out.println("현재 페이지 번호: " + pageResult.getNumber());
        System.out.println("페이지 사이즈: " + pageResult.getSize());

        System.out.println("검색 결과:");

        /*
        for(Board_dto resultDto : pageResult.getContent()){
            System.out.println(resultDto.getBoard_id());
        }*/

        while(pageResult.hasNext()){
            int nextPageNum = pageResult.nextPageable().getPageNumber();
            pageResult = boardService.getSearchPageBoard(tab, type, keyword, nextPageNum, InPageAmount);

            System.out.println("페이지 " + pageResult.getNumber() + " / " + pageResult.getTotalPages());
            for(Board_dto dto : pageResult.getContent()){
                System.out.println(dto.getBoard_id());
            }
        }
    }

    public void printBoardInfo(Board_dto dto){

        System.out.println("읽어온 게시판 아이디: "+dto.getBoard_id());
        System.out.println("읽어온 게시판 제목: "+dto.getTitle());
        System.out.println("읽어온 게시판 내용: "+dto.getContent());
        System.out.println("읽어온 게시판 탭: "+dto.getTab());
        System.out.println("읽어온 게시판 작성일: "+dto.getCreartedate());
        System.out.println("읽어온 게시판 수정일자: "+dto.getUpdatedate());
        System.out.println("읽어온 게시판 별점: "+dto.getRate());

        System.out.println("");
        System.out.println("읽어온 게시판 회원 아이디: "+dto.getUser_login_Id());
        System.out.println("읽어온 게시판 관광지 이름: "+dto.getAttraction_Name());
        System.out.println("읽어온 게시판 댓글 수: "+dto.getComment_Num());
        System.out.println("");

        System.out.println("읽어온 게시판 회원 번호: "+dto.getUser_id());
        System.out.println("읽어온 게시판 관광지 번호: "+dto.getAttraction_id());
        System.out.println("");
        System.out.println("");
        System.out.println("");
    }

    //싹 다 삭제
    public void resetAll(){
        boardRepository.deleteAll();
        userRepository.deleteAll();
    }

    //초기 유저 생성 메소드
    public void initSetting(){
        Calendar birthCalender=Calendar.getInstance();
        birthCalender.set(1995,Calendar.JUNE,1);
        Date birthDate=birthCalender.getTime();

        User user=new User();
        user.setUserLoginId("testuser");
        user.setPass("1111");
        user.setBirthDate(birthDate);
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

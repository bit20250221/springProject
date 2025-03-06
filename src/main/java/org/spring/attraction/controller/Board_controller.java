package org.spring.attraction.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.spring.attraction.dto.BoardImage_dto;
import org.spring.attraction.dto.Board_dto;
import org.spring.attraction.entity.Board;
import org.spring.attraction.service.BoardImage_service;
import org.spring.attraction.service.Board_service;
import org.spring.attraction.service.Comment_service;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/board")
@RequiredArgsConstructor
@Controller
@Log4j2
public class Board_controller {

    private final Board_service boardService;
    private final BoardImage_service boardImageService;
    private final Comment_service commentService;

    //검색, 페이징 기능 포함, 탭에 따라 ui 변경
    @GetMapping("/list")
    public String list(Model model,
                       @RequestParam(name = "pageAmount",defaultValue = "10") int pageAmount,
                       @RequestParam(name = "page", defaultValue = "0") int page,
                       @RequestParam(name = "type", defaultValue = "") String type,
                       @RequestParam(name = "tab",defaultValue = "") String tab,
                       @RequestParam(name = "Keyword",defaultValue = "") String Keyword)
    {
        Page<Board_dto> boardPageList=boardService.getSearchPageBoard(tab,type,Keyword,page,pageAmount);
        Integer boardSize=boardPageList.getSize();


        if(!boardPageList.isEmpty()) {
            model.addAttribute("result","NORMAL");
            model.addAttribute("pageNum",page);
            model.addAttribute("pageAmount",pageAmount);
            model.addAttribute("boardList", boardPageList);
            model.addAttribute("boardContent",boardPageList.getContent());
            model.addAttribute("boardSize", boardSize);
        }else{
            model.addAttribute("result","NONE");
        }
        model.addAttribute("tab",tab);
        return "boardList";
    }

    /*
    사용자와 탭, 글 작성자에 따라서 비공개 처리 로직이 있어야한다.(아직 미포함)
    탭(리뷰, 문의, 신고, 일반, 공지)에 따라 화면에 다르게 표현해야한다.)
    댓글 추가 로직 필요
    Principal 활용해서 사용자 아이디 가져온다.
    */

    @GetMapping("/getBoard/{id}")
    public String getBoard(Model model,
                           @PathVariable("id") Long id){

        Board_dto OneBoard=boardService.getBoard(id);
        //만약 탭이 문의, 신고인데, 작성자가 아니면 해당 화면을 보면 안된다.
        String OneTab=OneBoard.getTab().toString();
        if((OneTab.compareTo("문의")==0||OneTab.compareTo("신고")==0)){
            return "redirect:/board/list";
        }

        if(OneBoard!=null){
            model.addAttribute("result","NORMAL");
            model.addAttribute("board",OneBoard);
        }else{
            model.addAttribute("result","NONE");
        }

        return "getBoard";
    }

    //나중에 Spring Security 활용해서 권한 관련 기능 추가
    /*
    로그인된 사용자, 탭(리뷰, 문의, 신고, 일반, 공지)에 따라서 작성 제한 필요(탭 고르는 것을 제한),
    탭에 따라 글 작성 폼 UI 변화
    관광지 관리자로 접속하면 리뷰 상에서 별점 부여, 관광지 선택 제한 필요
    */
    @GetMapping("/insertBoard")
    public String InsertBoardView(Model model, Board_dto boardDto, String tab, HttpSession session){

        //탭과 사용자에 따라 다르게 적용(사용자의 권한 여부를 확인하는 로직 필요)
        //유저 관련은 일단 일시적인 값을 넣고 세션 활용
        session.setAttribute("orgTab",tab);
        session.setAttribute("userLoginId","testuser");
        if((tab.compareTo("문의")==0||tab.compareTo("공지")==0)){
            model.addAttribute("authority","권한이 없습니다");
            log.info("권한 부족!!!");
            return "redirect:/board/list";
        }else{
            log.info("게시글 작성 화면 출력!!!");
            model.addAttribute("tab",tab);
            //아래 두줄은 나중에 수정(로그인 기능에 따라서)
            model.addAttribute("writer","testuser");
            model.addAttribute("user_id",1L);
            switch (tab){
                case "일반","공지","신고","리뷰","문의":
                    return "insertBoardForm";
                default:
                    return "error";

            }
        }
    }

    //따로 유저 DTO, 관광지 DTO 받아오는 로직 필요, 검증 코드 필요(회원의 아이디, 탭, 파일)
    @PostMapping("/insertBoard")
    public String InsertBoardAction(Board_dto dto, HttpSession session){

        //만약 로그인된 사용자와 작성자 아이디 다르면 취소(사용자 아이디 조작 방지)(스프링 시큐리티 활용)(일단 세션 활용)
        if(dto.getUser_login_Id().compareTo(session.getAttribute("userLoginId").toString())!=0){
            return "redirect:/board/error";
        }
        //제출된 탭과 처음 입력하려는 탭을 비교할 필요 있음(탭 조작 방지)
        if(session.getAttribute("orgTab").toString().compareTo(dto.getTab())!=0){
            return "redirect:/board/error";
        }
        //관광지 확인 필요(실제 존재하는지 검증)

        //이미지도 본인이 해당 게시글에 올린게 맞는지 검증 필요(파일 조작 방지)
        Board writeBoard=boardService.writeBoard(dto);

        if(writeBoard!=null){
            List<BoardImage_dto> images=(List<BoardImage_dto>)session.getAttribute("tempImages");
            if(images!=null){
                for(int i=0;i<images.size();i++){
                      log.info("세션 내 파일 정보: "+images.get(i).getUUIDName());
                }
                for(BoardImage_dto tempImage:images){
                    boardImageService.saveImageFile(tempImage,writeBoard);
                }
            }
            session.invalidate();
            return "redirect:/board/list";
        }else{
            session.invalidate();
            return "redirect:/board/insertBoard";
        }
    }

    //오직 글 작성자만 수정 가능하도록 한다.(일단 파일 수정은 제외)
    @GetMapping("/updateBoard/{id}")
    public String updateBoardView(Model model,
                                  @PathVariable("id") Long id){
        Board_dto UpdateBoard=boardService.getBoard(id);
        if(UpdateBoard!=null){
            log.info("아이디 "+id+"글 수정화면 출력");
            model.addAttribute("result","NORMAL");
            model.addAttribute("board",UpdateBoard);
        }else{
            log.info("글이 존재하지 않음");
            model.addAttribute("result","NONE");
        }
        return "updateBoard";
    }

    //탭을 임의로 수정 못하도록 막는 로직 필요(일단 파일 수정은 제외)
    @PostMapping("/updateBoard/{id}")
    public String updateBoardAction(Board_dto dto){
        boardService.updateBoard(dto);
        log.info("아이디 "+dto.getBoard_id()+" 글 수정 완료");
        return "redirect:/board/getBoard/"+dto.getBoard_id();
    }

    //글 작성자만 삭제하도록 필요
    @PostMapping("/deleteBoard/{id}")
    public String deleteBoard(@PathVariable("id") Long id){
        boardService.deleteBoard(id);

        log.info("아이디 "+id+" 글 삭제 완료");
        return "redirect:/board/list";
    }
}

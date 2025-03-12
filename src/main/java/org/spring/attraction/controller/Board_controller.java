package org.spring.attraction.controller;

import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.spring.attraction.dto.AttractionDto;
import org.spring.attraction.dto.BoardImage_dto;
import org.spring.attraction.dto.Board_dto;
import org.spring.attraction.dto.user.UserDTO;
import org.spring.attraction.entity.Board;
import org.spring.attraction.repository.AttractionRepository;
import org.spring.attraction.repository.UserRepository;
import org.spring.attraction.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

@RequestMapping("/board")
@RequiredArgsConstructor
@Controller
@Log4j2
public class Board_controller {

    //나중에 구글 드라이브, AWS 등 외부 경로로 변경
    public final String UPLOAD_PATH = "C:/upload/board_images/";
    public final String TEMP_UPLOAD_PATH = "C:/upload/temp_board_images/";

    private final Board_service boardService;
    private final BoardImage_service boardImageService;
    private final Comment_service commentService;
    private final AttractionService attractionService;
    private final UserService userService;
    private final Board_SecurityService boardSecurityService;

    //추후 다양한 기능 추가시 삭제
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AttractionRepository attractionRepository;

    @PermitAll
    @GetMapping("/announce")
    public String announce(Model model,
                           @RequestParam(name = "pageAmount",defaultValue = "10") int pageAmount,
                           @RequestParam(name = "page", defaultValue = "0") int page,
                           @RequestParam(name = "type", defaultValue = "") String type,
                           @RequestParam(name = "Keyword",defaultValue = "") String Keyword)
    {
        String tab="공지";

        Page<Board_dto> boardPageList=boardService.getSearchPageBoard(tab,type,Keyword,page,pageAmount);
        Integer boardSize=boardPageList.getSize();


        if(!boardPageList.isEmpty()) {
            model.addAttribute("method","Announce");
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
        return "board/boardList";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/inquiry")
    public String inquiry(Model model,
                          @RequestParam(name = "pageAmount",defaultValue = "10") int pageAmount,
                          @RequestParam(name = "page", defaultValue = "0") int page,
                          @RequestParam(name = "type", defaultValue = "") String type,
                          @RequestParam(name = "Keyword",defaultValue = "") String Keyword)
    {
        String tab="문의";
        Page<Board_dto> boardPageList=boardService.getSearchPageBoard(tab,type,Keyword,page,pageAmount);
        Integer boardSize=boardPageList.getSize();

        if(!boardPageList.isEmpty()) {
            model.addAttribute("method","Inquiry");
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
        return "board/boardList";

    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/review")
    public String review(Model model,
                         @RequestParam(name = "pageAmount",defaultValue = "10") int pageAmount,
                         @RequestParam(name = "page", defaultValue = "0") int page,
                         @RequestParam(name = "type", defaultValue = "") String type,
                         @RequestParam(name = "Keyword",defaultValue = "") String Keyword)
    {
        String tab="리뷰";
        Page<Board_dto> boardPageList=boardService.getSearchPageBoard(tab,type,Keyword,page,pageAmount);
        Integer boardSize=boardPageList.getSize();

        if(!boardPageList.isEmpty()) {
            model.addAttribute("method","Review");
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
        return "board/boardList";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/normal")
    public String normal(Model model,
                         @RequestParam(name = "pageAmount",defaultValue = "10") int pageAmount,
                         @RequestParam(name = "page", defaultValue = "0") int page,
                         @RequestParam(name = "type", defaultValue = "") String type,
                         @RequestParam(name = "Keyword",defaultValue = "") String Keyword)
    {

        String tab="일반";
        Page<Board_dto> boardPageList=boardService.getSearchPageBoard(tab,type,Keyword,page,pageAmount);
        Integer boardSize=boardPageList.getSize();

        if(!boardPageList.isEmpty()) {
            model.addAttribute("method","Normal");
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
        return "board/boardList";

    }

    //검색, 페이징 기능 포함, 탭에 따라 ui 변경
    //(일반 사용자 기준, 해당 사용자가 하지 않은 신고와 문의는 안 보여준다)
    @PermitAll
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
            model.addAttribute("method","AllList");
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
        return "board/boardList";
    }

    //검색, 페이징 기능 포함, 탭에 따라 ui 변경(관리자 기준)(모든 정보 출력)



    /*
    사용자와 탭, 글 작성자에 따라서 비공개 처리 로직이 있어야한다.(아직 미포함)
    탭(리뷰, 문의, 신고, 일반, 공지)에 따라 화면에 다르게 표현해야한다.)
    댓글 추가 로직 필요
    Principal 활용해서 사용자 아이디 가져온다.
    */

    @GetMapping("/getBoard/{id}")
    public String getBoard(Model model,
                           @PathVariable("id") Long id, Principal principal){

        Board_dto OneBoard=boardService.getBoard(id);
        String Auth=SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator().next().getAuthority();
        UserDTO OneUser=boardSecurityService.getUser();
        //만약 탭이 문의, 신고인데, 작성자 또는 해당 관광지가 아니면 해당 화면을 보면 안된다.
        String OneTab=OneBoard.getTab().toString();
        if((OneTab.compareTo("신고")==0) &&(!OneBoard.getUser_login_Id().equals(principal.getName())
                && Auth.compareTo("manager")!=0)){
            log.info(Auth);
            log.info("1 "+principal.getName() + " User is Not Authenticated");
            return "redirect:/board/list";
        }

        //문의면 문의 관리자, 작성자 또는 문의의 대상이 되는 관광지만 봐야한다
        if(OneTab.compareTo("문의") == 0 &&
                !OneBoard.getUser_login_Id().equals(principal.getName()) &&  // 작성자가 아님
                !(Auth.compareTo("attraction") == 0 && OneBoard.getAttraction_id().equals(OneUser.getAttraction())) &&  // 관광지 소속 사용자인 경우 예외
                !(Auth.compareTo("manager") == 0))
        {
            log.info(Auth);
            log.info("1 "+principal.getName() + " User is Not Authenticated");
            return "redirect:/board/list";
        }

        if(OneBoard!=null){
            model.addAttribute("result","NORMAL");
            model.addAttribute("board",OneBoard);
        }else{
            model.addAttribute("result","NONE");
        }

        return "board/getBoard";
    }

    //나중에 Spring Security 활용해서 권한 관련 기능 추가
    /*
    로그인된 사용자, 탭(리뷰, 문의, 신고, 일반, 공지)에 따라서 작성 제한 필요(탭 고르는 것을 제한),
    탭에 따라 글 작성 폼 UI 변화
    관광지 관리자로 접속하면 리뷰 상에서 별점 부여, 관광지 선택 제한 필요
    */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/insertBoard")
    public String InsertBoardView(Model model, Board_dto boardDto, HttpSession session, String tab, Principal principal){

        //탭과 사용자에 따라 다르게 적용(사용자의 권한 여부를 확인하는 로직 필요)
        //유저 관련은 일단 일시적인 값을 넣고 세션 활용
        String Auth=SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator().next().getAuthority();
        session.setAttribute("orgTab",tab);
        if(tab.compareTo("공지")==0 && Auth.compareTo("manager")!=0){
            log.info("{} User is Not Authenticated", principal.getName());
            model.addAttribute("authority","권한이 없습니다");
            log.info("권한 부족!!!");
            return "redirect:/board/list";
        }else{
            log.info("게시글 작성 화면 출력!!!");
            model.addAttribute("tab",tab);

            //서버 내 저장된 관광지 추가
            List<AttractionDto> attractionList=attractionService.findAll();
            model.addAttribute("attractionList",attractionList);

            //아래 두줄은 스프링 시큐리티에서 값을 가져온다
            model.addAttribute("writer",principal.getName());
            UserDTO user=boardSecurityService.getUser();
            model.addAttribute("user_id",user.getId());
            switch (tab){
                case "일반","공지","신고","리뷰","문의":
                    return "board/insertBoardForm";
                default:
                    return "board/error";

            }
        }
    }

    //따로 유저 DTO, 관광지 DTO 받아오는 로직 필요, 검증 코드 필요(회원의 아이디, 탭, 파일)
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/insertBoard")
    public String InsertBoardAction(Board_dto dto, HttpSession session, Principal principal){
        //만약 로그인된 사용자와 작성자 아이디 다르면 취소(사용자 아이디 조작 방지)(스프링 시큐리티 활용)
        if(dto.getUser_login_Id().compareTo(principal.getName())!=0){
            session.removeAttribute("tempImages");
            session.removeAttribute("orgTab");
            return "redirect:/board/error";
        }
        //제출된 탭과 처음 입력하려는 탭을 비교할 필요 있음(탭 조작 방지)
        if(session.getAttribute("orgTab").toString().compareTo(dto.getTab())!=0){
            session.removeAttribute("tempImages");
            session.removeAttribute("orgTab");
            return "redirect:/board/error";
        }

        /*
            관광지 확인 필요(실제 존재하는지 검증), 만약 기타에 db에 없는 관광지를 입력했다면
            기존의 라디오 선택을 무시하고 기타내 입력된 값을 저장(attraction_id는 null)
        */

        //기타 옵션을 선택한 경우 값이 존재
        String attractionName=dto.getAttraction_Name();
        if(attractionName!=null &&!attractionName.equals("기타")){
            Long attraction_id = dto.getAttraction_id();
            System.out.println(attraction_id);
            if(attraction_id!=null) {
                if (attractionService.findById(attraction_id) == null) {
                    //임시 이미지 삭제 필요
                    session.removeAttribute("tempImages");
                    session.removeAttribute("orgTab");
                    return "redirect:/board/error";
                }
                dto.setAttraction_id(attraction_id);
            }
            dto.setAttraction_Name(attractionName);
        }

        //이미지도 본인이 해당 게시글에 올린게 맞는지 검증 필요(파일 조작 방지)
        Board writeBoard=boardService.writeBoard(dto);

        if(writeBoard!=null){
            //파일이 등록전에 임의로 삭제됨(원인 불명);;
            List<BoardImage_dto> images=(List<BoardImage_dto>)session.getAttribute("tempImages");
            if(images!=null){
                for(int i=0;i<images.size();i++){
                      log.info("세션 내 파일 정보: "+images.get(i).getUUIDName());
                }
                for(BoardImage_dto tempImage:images){
                    if(!boardImageService.saveImageFile(tempImage,writeBoard)){
                        log.info("파일 정식 등록 메소드 실행중 오류 발생");
                        session.removeAttribute("tempImages");
                        //모든 임시 파일 삭제
                        for(BoardImage_dto delete:images){
                            if(delete!=null&&delete.getUUIDName()!=null && delete.getUUID()!=null){
                                boardImageService.deleteTempImageFile(delete.getUUID(), delete.getUUIDName());
                            }
                        }
                        //이미 등록된 파일들도 삭제

                        return "redirect:/board/list";
                    }
                }
            }
            session.removeAttribute("tempImages");
            session.removeAttribute("orgTab");
            return "redirect:/board/list";
        }else{
            //임시 이미지 삭제 메소드 필요
            session.removeAttribute("tempImages");
            session.removeAttribute("orgTab");
            return "redirect:/board/insertBoard";
        }
    }

    //오직 글 작성자만 수정 가능하도록 한다.(일단 파일 수정은 제외)
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/updateBoard/{id}")
    public String updateBoardView(Model model,
                                  @PathVariable("id") Long id, Board_dto boardDTO, HttpSession session, Principal principal){
        Board_dto UpdateBoard=boardService.getBoard(id);

        UserDTO user=boardSecurityService.getUser();
        log.info("this is User info: \n id: {}, loginid: {}, password: {}, birthdate: {}, usertype: {}, grade: {}",
              user.getId(), user.getUserLoginId(), user.getPass(), user.getBirthDate(), user.getUserType(), user.getGrade());

        //만약 로그인된 사용자와 작성자 아이디가 일치하지 않는다면 (사용자 아이디 조작 방지)(스프링 시큐리티 활용));
        if(UpdateBoard.getUser_login_Id().compareTo(user.getUserLoginId())!=0
            ||UpdateBoard.getUser_id() != user.getId())
        {

            log.info("{} User is Not Authenticated", principal.getName());

            return "redirect:/board/getBoard/"+UpdateBoard.getBoard_id();
        }

        if(UpdateBoard.getTab().compareTo("문의")==0||UpdateBoard.getTab().compareTo("리뷰")==0
        ||UpdateBoard.getTab().compareTo("신고")==0||UpdateBoard.getTab().compareTo("공지")==0){

            //기존에 등록되었던 이미지 정보 추가
            List<BoardImage_dto> images = boardImageService.getImagesByBoardId(id);
            session.setAttribute("UpdateImages",images);
        }

        //서버 내 저장된 관광지 추가
        String tab=UpdateBoard.getTab();
        if(UpdateBoard!=null){
            log.info("아이디 "+id+"글 수정화면 출력");
            List<AttractionDto> attractionList=attractionService.findAll();
            model.addAttribute("attractionList",attractionList);
            model.addAttribute("result","NORMAL");
            model.addAttribute("board",UpdateBoard);
            model.addAttribute("tab",tab);
        }else{
            log.info("글이 존재하지 않음");
            model.addAttribute("result","NONE");
            return "board/error";
        }
        return "board/updateBoard";
    }

    /*
        탭을 임의로 수정 못하도록 막는 로직 필요
        그리고 현재 로그인 정보를 가져올 필요가 있음(일단은 form으로 가져오고 나중에 principal 객체를 이용해 처리)
    */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/updateBoard/{id}")
    public String updateBoardAction(@PathVariable("id") Long id, Board_dto boardDTO, HttpSession session, Principal principal){
        log.info(id+" board info: {},{},{},{},{},{},{},{},{}",
                boardDTO.getBoard_id(), boardDTO.getTab(), boardDTO.getTitle(),
                boardDTO.getContent(), boardDTO.getAttraction_id(), boardDTO.getUser_id(),
                boardDTO.getRate(), boardDTO.getCreartedate(), boardDTO.getUpdatedate());

        if(boardDTO.getUser_login_Id().compareTo(principal.getName())!=0){
            log.info("해당 글 작성자가 아닙니다.");
            return "redirect:/board/error";
        }
        Board_dto UpdateBoard=boardService.updateBoard(boardDTO);
        if(UpdateBoard!=null&&UpdateBoard.getTab().compareTo("일반")!=0) {

            List<BoardImage_dto> images = (List<BoardImage_dto>) session.getAttribute("UpdateImages");
            if (!images.isEmpty()) {
                log.info("세션 내 파일 정보 감지: {}개", images.size());
                for (BoardImage_dto UpdateImage : images) {
                    String url = UpdateImage.getImagePath();
                    log.info("세션 내 이미지 url: {}", url);
                    //임시 폴더에 업로드된 이미지면(신규 이미지)
                    if (url.contains(TEMP_UPLOAD_PATH)) {
                        String NewPath=UPLOAD_PATH + UpdateImage.getUUIDName();
                        boardImageService.saveImageFile(UpdateImage, Board_service.toEntity(UpdateBoard
                                ,userRepository.getReferenceById(UpdateBoard.getUser_id())
                                ,attractionRepository.getReferenceById(UpdateBoard.getAttraction_id())
                                ,null));

                    }
                }
            }
        }
        session.removeAttribute("UpdateImages");
        log.info("아이디 "+boardDTO.getBoard_id()+" 글 수정 완료");
        return "redirect:/board/getBoard/"+boardDTO.getBoard_id();
    }

    //글 작성자만 삭제하도록 필요
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/deleteBoard/{id}")
    public String deleteBoard(@PathVariable("id") Long id, Principal principal){
        String CurrentUser=principal.getName();

        boardService.deleteBoard(id);

        log.info(CurrentUser+" 유저의 아이디 "+id+" 글 삭제 완료");
        return "redirect:/board/list";
    }
}

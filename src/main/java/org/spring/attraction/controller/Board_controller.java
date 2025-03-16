package org.spring.attraction.controller;

import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.spring.attraction.dto.*;
import org.spring.attraction.entity.Attraction;
import org.spring.attraction.entity.Board;
import org.spring.attraction.entity.User;
import org.spring.attraction.repository.AttractionRepository;
import org.spring.attraction.repository.UserRepository;
import org.spring.attraction.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URLEncoder;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
                           @AuthenticationPrincipal UserDetails userDetails,
                           @RequestParam(name = "pageAmount",defaultValue = "10") int pageAmount,
                           @RequestParam(name = "page", defaultValue = "0") int page,
                           @RequestParam(name = "type", defaultValue = "") String type,
                           @RequestParam(name = "Keyword",defaultValue = "") String Keyword)
    {
        String tab="공지";
        Page<Board_dto> boardPageList=boardService.getSearchPageBoard(tab,type,Keyword,page,pageAmount);
        Integer boardSize=boardPageList.getSize();

        model.addAttribute("userRole", userService.getUserRole(userDetails));
        model.addAttribute("method","Announce");
        model.addAttribute("result","NORMAL");
        if(!boardPageList.isEmpty()) {
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
                          @AuthenticationPrincipal UserDetails userDetails,
                          @RequestParam(name = "pageAmount",defaultValue = "10") int pageAmount,
                          @RequestParam(name = "page", defaultValue = "0") int page,
                          @RequestParam(name = "type", defaultValue = "") String type,
                          @RequestParam(name = "Keyword",defaultValue = "") String Keyword)
    {
        String tab="문의";
        Page<Board_dto> boardPageList=boardService.getSearchPageBoard(tab,type,Keyword,page,pageAmount);
        Integer boardSize=boardPageList.getSize();

        model.addAttribute("userRole", userService.getUserRole(userDetails));
        model.addAttribute("method","Inquiry");
        model.addAttribute("result","NORMAL");
        if(!boardPageList.isEmpty()) {
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
                         @AuthenticationPrincipal UserDetails userDetails,
                         @RequestParam(name = "pageAmount",defaultValue = "10") int pageAmount,
                         @RequestParam(name = "page", defaultValue = "0") int page,
                         @RequestParam(name = "type", defaultValue = "") String type,
                         @RequestParam(name = "Keyword",defaultValue = "") String Keyword)
    {
        String tab="리뷰";
        Page<Board_dto> boardPageList = null;
        if(type.compareTo("attraction")!=0) {
            boardPageList=boardService.getSearchPageBoard(tab,type,Keyword,page,pageAmount);
        }else{
            boardPageList = boardService.getSearchReviewBoard(Keyword,page,pageAmount);
        }

        Integer boardSize=boardPageList.getSize();

        model.addAttribute("userRole", userService.getUserRole(userDetails));
        model.addAttribute("method","Review");
        model.addAttribute("result","NORMAL");
        if(!boardPageList.isEmpty()) {
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

    @PermitAll
    @GetMapping("/normal")
    public String normal(Model model,
                         @AuthenticationPrincipal UserDetails userDetails,
                         @RequestParam(name = "pageAmount",defaultValue = "10") int pageAmount,
                         @RequestParam(name = "page", defaultValue = "0") int page,
                         @RequestParam(name = "type", defaultValue = "") String type,
                         @RequestParam(name = "Keyword",defaultValue = "") String Keyword)
    {

        String tab="일반";
        Page<Board_dto> boardPageList=boardService.getSearchPageBoard(tab,type,Keyword,page,pageAmount);
        Integer boardSize=boardPageList.getSize();

        model.addAttribute("userRole", userService.getUserRole(userDetails));
        model.addAttribute("method","Normal");
        model.addAttribute("result","NORMAL");
        if(!boardPageList.isEmpty()) {
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
    @GetMapping("/report")
    public String report(Model model,
                         @AuthenticationPrincipal UserDetails userDetails,
                         @RequestParam(name = "pageAmount",defaultValue = "10") int pageAmount,
                         @RequestParam(name = "page", defaultValue = "0") int page,
                         @RequestParam(name = "type", defaultValue = "") String type,
                         @RequestParam(name = "Keyword",defaultValue = "") String Keyword)
    {

        String tab="신고";
        Page<Board_dto> boardPageList=boardService.getSearchPageBoard(tab,type,Keyword,page,pageAmount);
        Integer boardSize=boardPageList.getSize();

        model.addAttribute("userRole", userService.getUserRole(userDetails));
        model.addAttribute("method","Report");
        model.addAttribute("result","NORMAL");
        if(!boardPageList.isEmpty()) {
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
    @PermitAll
    @GetMapping("/list")
    public String list(Model model,
                       @AuthenticationPrincipal UserDetails userDetails,
                       @RequestParam(name = "pageAmount",defaultValue = "10") int pageAmount,
                       @RequestParam(name = "page", defaultValue = "0") int page,
                       @RequestParam(name = "type", defaultValue = "") String type,
                       @RequestParam(name = "tab",defaultValue = "") String tab,
                       @RequestParam(name = "Keyword",defaultValue = "") String Keyword)
    {
        Page<Board_dto> boardPageList=boardService.getSearchPageBoard(tab,type,Keyword,page,pageAmount);
        Integer boardSize=boardPageList.getSize();

        model.addAttribute("userRole", userService.getUserRole(userDetails));
        model.addAttribute("method","AllList");
        model.addAttribute("result","NORMAL");
        if(!boardPageList.isEmpty()) {
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

    /*
    사용자와 탭, 글 작성자에 따라서 비공개 처리 로직이 있어야한다.
    탭(리뷰, 문의, 신고, 일반, 공지)에 따라 화면에 다르게 표현해야한다.)
    댓글 추가 로직 필요
    Principal 활용해서 사용자 아이디 가져온다.
    */

    @GetMapping("/getBoard/{id}")
    public String getBoard(Model model,
                           @AuthenticationPrincipal UserDetails userDetails,
                           @PathVariable("id") Long id, Principal principal, RedirectAttributes redirectAttributes) {

        model.addAttribute("userRole", userService.getUserRole(userDetails));
        String message;
        Board_dto OneBoard=boardService.getBoard(id);
        ArrayList<Comment_dto> commentList= (ArrayList<Comment_dto>) commentService.getCommentsByBoard(id);
        if(OneBoard==null){
            model.addAttribute("result","NONE");
            message="NONE EXIST BOARD";
            redirectAttributes.addFlashAttribute("message",message);
            return "redirect:/board/list";
        }

        String OneTab=OneBoard.getTab().toString();
        String Auth=SecurityContextHolder.getContext()
                .getAuthentication().getAuthorities()
                .iterator().next().getAuthority();
        UserDto OneUser=boardSecurityService.getUser();

        //만약 탭이 문의, 신고인데, 작성자 또는 해당 관광지가 아니면 해당 화면을 보면 안된다.
        if((OneTab.compareTo("문의")==0 || OneTab.compareTo("신고")==0) && (principal==null)){
            message="아이디 " +OneBoard.getBoard_id()+ " 문의, 신고 게시물에 비로그인 사용자 접근 차단";
            log.info(message);
            redirectAttributes.addFlashAttribute("message", message);
            return "redirect:/";
        }
        if((OneTab.compareTo("신고")==0)&&(principal!=null) &&(!OneBoard.getUser_login_Id().equals(principal.getName())
                && Auth.compareTo("manager")!=0)){
            message=principal.getName() + " User is Not Authenticated about this report";
            log.info(Auth);
            log.info(message);
            redirectAttributes.addFlashAttribute("message",message);
            return "redirect:/board/report";
        }

        //문의면 문의 관리자, 작성자 또는 문의의 대상이 되는 관광지만 봐야한다
        if(OneTab.compareTo("문의") == 0 &&
                !OneBoard.getUser_login_Id().equals(principal.getName()) &&  // 작성자가 아님
                !(Auth.compareTo("attraction") == 0 && OneBoard.getAttraction_id().equals(OneUser.getAttraction())) &&  // 관광지 소속 사용자인 경우 예외
                !(Auth.compareTo("manager") == 0))
        {
            log.info(Auth);
            message=principal.getName() + " User is Not Authenticated about this inquiry";
            log.info(message);
            redirectAttributes.addFlashAttribute("message",message);
            return "redirect:/board/inquiry";
        }

        if(OneBoard!=null){
            /*
            만약 해당 관광지 리뷰가 관광지 본인 계정으로 작성한 리뷰이면 그
            것을 알려줘야한다.(즉 제목에 (관광지 계정입니다.) 라고 표시해줘야한다.)
            */
            Long BoardAttractionId= OneBoard.getAttraction_id();
            if(BoardAttractionId!=null) {
                Optional<User> optionalUser = userRepository.findById(OneBoard.getUser_id());

                Attraction writerattraction;
                if (optionalUser.isPresent()) {
                    User isExist = optionalUser.get();
                    writerattraction = isExist.getAttraction();
                    if (writerattraction != null
                            && BoardAttractionId == writerattraction.getId()) {
                        OneBoard.setRate(0);
                        OneBoard.setTitle(OneBoard.getTitle() + " (본인 관광지 계정입니다.)");
                    }
                }
            }
            model.addAttribute("commentlist",commentList);
            model.addAttribute("result","NORMAL");
            model.addAttribute("board",OneBoard);
        }

        return "board/getBoard";
    }

    /*
    로그인된 사용자, 탭(리뷰, 문의, 신고, 일반, 공지)에 따라서 작성 제한 필요(탭 고르는 것을 제한),
    탭에 따라 글 작성 폼 UI 변화
    관광지 관리자로 접속하면 리뷰 상에서 별점 부여, 관광지 선택 제한 필요
    */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/insertBoard")
    public String InsertBoardView(Model model,
                                  @AuthenticationPrincipal UserDetails userDetails,
                                  Board_dto boardDto,
                                  HttpSession session,
                                  @RequestParam("tab") String tab,
                                  Principal principal,
                                  RedirectAttributes redirectAttributes){

        model.addAttribute("userRole", userService.getUserRole(userDetails));
        //탭과 사용자에 따라 다르게 적용
        String message;
        String Auth=SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator().next().getAuthority();
        Board_dto board=new Board_dto();
        board.setTab(tab);
        if(principal ==null){
            message="비로그인 사용자의 부적절한 접근입니다.";
            redirectAttributes.addFlashAttribute("message",message);
            return "redirect:/";
        }
        if(tab.compareTo("공지")==0 && Auth.compareTo("manager")!=0){
            log.info("{} User is Not Authenticated", principal.getName());
            model.addAttribute("authority","권한이 없습니다");
            message="Only Manager can create announcements";
            redirectAttributes.addFlashAttribute("message",message);
            return "redirect:/board/announce";
        }else{
            log.info("게시글 작성 화면 출력!!!");
            model.addAttribute("boardDto",board);
            model.addAttribute("tab",tab);

            //서버 내 저장된 관광지 추가
            List<AttractionDto> attractionList=attractionService.findAll();
            model.addAttribute("attractionList",attractionList);

            //아래 두줄은 스프링 시큐리티에서 값을 가져온다
            model.addAttribute("writer",principal.getName());
            UserDto user=boardSecurityService.getUser();
            model.addAttribute("user_id",user.getId());
            switch (tab){
                case "일반","공지","신고","리뷰","문의":
                    return "board/insertBoardForm";
                default:
                    message="NONE EXIST TAB ABOUT INSERT FORM";
                    redirectAttributes.addFlashAttribute("message",message);
                    return "redirect:board/error";

            }
        }
    }

    //따로 유저 DTO, 관광지 DTO 받아오는 로직 필요, 검증 코드 필요(회원의 아이디, 탭, 파일)
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/insertBoard")
    public String InsertBoardAction(Board_dto dto, HttpSession session,@RequestParam("activeTab") String activeTab,Principal principal,RedirectAttributes redirectAttributes){
        //만약 로그인된 사용자와 작성자 아이디 다르면 취소(사용자 아이디 조작 방지)(스프링 시큐리티 활용)
        String message;
        User currentUser=boardSecurityService.getUserEntity();
        String auth=SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator().next().getAuthority();
        if(dto.getUser_login_Id().compareTo(principal.getName())!=0){
            session.removeAttribute("tempImages");
            message="작성자 정보와 로그인된 사용자 아이디가 다릅니다.";
            redirectAttributes.addFlashAttribute("message",message);
            return "redirect:board/error";
        }
        if(dto.getTab().compareTo("공지")==0 && auth.compareTo("manager")!=0){
            message="관리자만 공지 작성할 수 있습니다.";
            session.removeAttribute("tempImages");
            redirectAttributes.addFlashAttribute("message", message);
            try {
                return "redirect:/board/insertBoard?tab=" + URLEncoder.encode(activeTab, "UTF-8");
            }
            catch (Exception e){
                e.printStackTrace();
                return "redirect:board/error";
            }
        }
        /*
            관광지 확인 필요(실제 존재하는지 검증), 만약 기타에 db에 없는 관광지를 입력했다면
            기존의 라디오 선택을 무시하고 기타 내 입력된 값을 저장(attraction_id는 null)
        */
        Long attraction_id = dto.getAttraction_id();
        Long writer_attraction_id=currentUser.getAttraction() != null
                ? currentUser.getAttraction().getId() : null;
        if(auth.compareTo("attraction")==0 && attraction_id != writer_attraction_id) {
            message = "본인 관광지 리뷰만 작성 가능합니다.";
            session.removeAttribute("tempImages");
            redirectAttributes.addFlashAttribute("message", message);
            try {
                return "redirect:/board/insertBoard?tab=" + URLEncoder.encode(activeTab, "UTF-8");
            }
            catch (Exception e){
                e.printStackTrace();
                return "redirect:board/error";
            }
        }

        //기타 옵션을 선택한 경우 값이 존재
        String attractionName=dto.getAttraction_Name();
        if(attractionName!=null &&!attractionName.equals("기타")){
            dto.setAttraction_Name(attractionName);
        }else if(attraction_id!=null){
            if (attractionService.findById(attraction_id) == null) {
                session.removeAttribute("tempImages");
                message = "등록된 관광지 정보가 서버상에 존재하지 않습니다.";
                redirectAttributes.addFlashAttribute("message", message);
                try {
                    return "redirect:/board/insertBoard?tab=" + URLEncoder.encode(activeTab, "UTF-8");
                }catch (Exception e){
                    e.printStackTrace();
                    return "redirect:board/error";
                }
            }
            dto.setAttraction_id(attraction_id);
    }

        //이미지도 본인이 해당 게시글에 올린게 맞는지 검증 필요(파일 조작 방지)
        Board writeBoard=boardService.writeBoard(dto);

        if(writeBoard!=null){

            List<BoardImage_dto> images=(List<BoardImage_dto>)session.getAttribute("tempImages");
            List<MultipartFile> dtoimages=dto.getImglist();
            if(dtoimages!=null && !dtoimages.isEmpty()&& images!=null&& !images.isEmpty()){
                int i=0;
                try {
                    for(BoardImage_dto image : images) {
                        if(!boardImageService.saveImageFile(writeBoard, image, dtoimages.get(i))) {
                            log.info("파일 정식 등록 메소드 실행중 오류 발생");
                            session.removeAttribute("tempImages");
                            message = "이미지 등록 중 오류 발생";
                            redirectAttributes.addFlashAttribute("message", message);
                            return "redirect:/board/insertBoard?tab="+ URLEncoder.encode(activeTab, "UTF-8");
                        }
                        i++;
                    }
                }catch(Exception e) {
                    log.info("파일 등록 메소드 실행중 오류 발생: {}", e.getMessage());
                    session.removeAttribute("tempImages");
                    message = "이미지를 한번에 업로드 해주세요!!";
                    redirectAttributes.addFlashAttribute("message", message);
                    try {
                        return "redirect:/board/insertBoard?tab=" + URLEncoder.encode(activeTab, "UTF-8");
                    }catch (Exception e2){
                        e2.printStackTrace();
                        return "redirect:board/error";
                    }
                }
            }

            session.removeAttribute("tempImages");
            message=writeBoard.getId()+" 번 게시글 등록 완료";
            redirectAttributes.addFlashAttribute("message",message);
            return "redirect:/board/getBoard/"+writeBoard.getId();
        }else{
            session.removeAttribute("tempImages");
            message="게시글 등록 실패";
            redirectAttributes.addFlashAttribute("message", message);
            try {
                return "redirect:/board/insertBoard?tab=" + URLEncoder.encode(activeTab, "UTF-8");
            }
            catch (Exception e){
                e.printStackTrace();
                return "redirect:board/error";
            }
        }
    }

    //오직 글 작성자만 수정 가능하도록 한다.(파일 수정은 제외)
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/updateBoard/{id}")
    public String updateBoardView(Model model,
                                  @AuthenticationPrincipal UserDetails userDetails,
                                  @PathVariable("id") Long id,
                                  Board_dto boardDTO,
                                  HttpSession session,
                                  Principal principal,
                                  RedirectAttributes redirectAttributes){

        model.addAttribute("userRole", userService.getUserRole(userDetails));
        Board_dto UpdateBoard=boardService.getBoard(id);
        String message;
        UserDto user=boardSecurityService.getUser();
        log.info("this is User info: \n id: {}, loginid: {}, password: {}, birthdate: {}, usertype: {}, grade: {}",
              user.getId(), user.getUserLoginId(), user.getPass(), user.getBirthDate(), user.getUserType(), user.getGrade());

        if(UpdateBoard==null){
            log.info("글이 존재하지 않음");
            model.addAttribute("result","NONE");
            message="NONE BOARD";
            redirectAttributes.addFlashAttribute("message",message);
            return "redirect:board/error";
        }

        //만약 로그인된 사용자와 작성자 아이디가 일치하지 않는다면 (사용자 아이디 조작 방지)(스프링 시큐리티 활용));
        if(UpdateBoard.getUser_login_Id().compareTo(user.getUserLoginId())!=0
            ||UpdateBoard.getUser_id() != user.getId())
        {
            message= principal.getName()+" User is Not Authenticated";
            log.info(message);
            redirectAttributes.addFlashAttribute("message",message);
            return "redirect:/board/getBoard/"+UpdateBoard.getBoard_id();
        }

        if(UpdateBoard.getTab().compareTo("문의")==0||UpdateBoard.getTab().compareTo("리뷰")==0
        ||UpdateBoard.getTab().compareTo("신고")==0||UpdateBoard.getTab().compareTo("공지")==0){

            //기존에 등록되었던 이미지 정보 추가
            List<BoardImage_dto> images = boardImageService.getImagesByBoardId(id);
            session.setAttribute("CurrentImage",images);
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
        }
        return "board/updateBoard";
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/updateBoard/{id}")
    public String updateBoardAction(@PathVariable("id") Long id,
                                    Board_dto boardDTO,
                                    HttpSession session,
                                    Principal principal,
                                    RedirectAttributes redirectAttributes){
        log.info(id+" board info: {},{},{},{},{},{},{},{},{}",
                boardDTO.getBoard_id(), boardDTO.getTab(), boardDTO.getTitle(),
                boardDTO.getContent(), boardDTO.getAttraction_id(), boardDTO.getUser_id(),
                boardDTO.getRate(), boardDTO.getCreartedate(), boardDTO.getUpdatedate());
        String message;
        if(boardDTO.getUser_login_Id().compareTo(principal.getName())!=0){
            message="해당 글 작성자가 아닙니다.";
            log.info(message);
            redirectAttributes.addFlashAttribute("message",message);
            return "redirect:board/error";
        }
        Board_dto UpdateBoard=boardService.updateBoard(boardDTO);
        if(UpdateBoard!=null&&UpdateBoard.getTab().compareTo("일반")!=0) {

            List<BoardImage_dto> images = (List<BoardImage_dto>) session.getAttribute("UpdateImages");
            List<MultipartFile> multipartFiles=boardDTO.getImglist();
            if (images!=null && !images.isEmpty()) {
                log.info("세션 내 파일 정보 감지: {}개", images.size());
                for (int i=0;i<multipartFiles.size();i++) {
                    BoardImage_dto UpdateImage=images.get(i);
                    String url = UpdateImage.getImagePath();
                    log.info("세션 내 이미지 url: {}", url);
                    //임시 폴더에 업로드된 이미지면(신규 이미지)
                    if(UpdateBoard.getAttraction_id()!=null) {
                        boardImageService.saveImageFile(Board_service.toEntity(UpdateBoard
                                , userRepository.getReferenceById(UpdateBoard.getUser_id())
                                , attractionRepository.getReferenceById(UpdateBoard.getAttraction_id())
                                , null), UpdateImage, multipartFiles.get(i));

                    }else{
                        boardImageService.saveImageFile(Board_service.toEntity(UpdateBoard
                                , userRepository.getReferenceById(UpdateBoard.getUser_id())
                                , null
                                , null), UpdateImage, multipartFiles.get(i));
                    }
                }
            }
        }
        session.removeAttribute("UpdateImages");
        session.removeAttribute("CurrentImage");
        message="아이디 "+boardDTO.getBoard_id()+" 글 수정 완료";
        log.info(message);
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/board/getBoard/"+boardDTO.getBoard_id();
    }

    //글 작성자만 삭제하도록 필요
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/deleteBoard/{id}")
    public String deleteBoard(@PathVariable("id") Long id,
                              Principal principal,
                              RedirectAttributes redirectAttributes){
        String CurrentUser=principal.getName();
        String message;
        if(boardService.deleteBoard(id)) {
            message = "아이디 " + CurrentUser + "의 " + id + "번 글 삭제 완료";
            log.info(message);
            redirectAttributes.addFlashAttribute("message", message);
            return "redirect:/board/list";
        }else{
            message="아이디 " + CurrentUser + "의 " + id + "번 글 삭제 실패";
            redirectAttributes.addFlashAttribute("message", message);
            return "redirect:/board/getBoard/"+id;
        }
    }
}

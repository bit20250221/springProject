package org.spring.attraction.controller;

import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.spring.attraction.dto.BoardImage_dto;
import org.spring.attraction.dto.Board_dto;
import org.spring.attraction.dto.user.UserDTO;
import org.spring.attraction.service.BoardImage_service;
import org.spring.attraction.service.Board_SecurityService;
import org.spring.attraction.service.Board_service;
import org.spring.attraction.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/image")
@RequiredArgsConstructor
@RestController
@Log4j2
public class BoardImage_controller {

    @Autowired
    private BoardImage_service boardImageService;

    @Autowired
    private Board_service boardService;

    private final UserService userService;
    private final Board_SecurityService boardSecurityService;

    //이미지 임시 등록(파일 스토리지에 이미지 저장, DB 상에 이미지 정보 저장, 세션에 이미지 dto를 저장)
    //한 사용자가 하나의 창만 이용해서 기능 수행하면 아무런 문제가 없음
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/TempSave")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> saveTempImage(@RequestParam("tempImage") MultipartFile tempImage, HttpSession session) {
        log.info("Temp Save Image");

        Map<String, Object> response = new HashMap<>();

        //파일 보안검사 필요

        try {
            BoardImage_dto tempFile = BoardImage_dto.boardImageDto2(tempImage);
            BoardImage_dto saveTempImageFile = boardImageService.saveTempImageFile(tempImage,tempFile);
            String ImageUrl = saveTempImageFile.getImagePath();

            //세션의 기존 이미지 dto 리스트에 등록하거나, 새로운 이미지 dto 리스트 생성 후 세션에 등록
            List<BoardImage_dto> sessionImages = (List<BoardImage_dto>) session.getAttribute("tempImages");
            if (sessionImages == null) {
                sessionImages = new ArrayList<>();
            }
            sessionImages.add(saveTempImageFile);
            session.setAttribute("tempImages", sessionImages);

            response.put("ImageUrl", ImageUrl);
            response.put("ImageName", saveTempImageFile.getName());
            response.put("ImageUUIDName",saveTempImageFile.getUUIDName());
            response.put("ImageDTO", saveTempImageFile);
            response.put("message", "Success");

        }catch (Exception e) {
            e.printStackTrace();
            response.put("message","Fail");
        }
        return ResponseEntity.ok(response);
    }

    //이미지 등록 2(기존의 게시글 수정 화면에서 이용)(검증 필요)
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/ImageUpdates")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> saveImage(@RequestParam("boardId") Long boardId, MultipartFile[] images, HttpSession session) {
        log.info("게시글 내 새로운 이미지 임시 등록");
        UserDTO userDto=boardSecurityService.getUser();
        Map<String, Object> response = new HashMap<>();
        Board_dto boardDto=boardService.getBoard(boardId);

        if(boardDto==null){
            response.put("message", "NOBoard");
            return ResponseEntity.ok(response);
        }
        if(boardDto.getUser_login_Id().compareTo(userDto.getUserLoginId())!=0){
            response.put("message", "NOPermission");
            return ResponseEntity.ok(response);
        }
        //이미지 수정 화면에 있던 기존 이미지가 저장된 세션 정보를 가져옴
        List<BoardImage_dto> sessionImages = (List<BoardImage_dto>) session.getAttribute("UpdateImages");

        if(sessionImages==null){
            log.info("세션에 등록할 리스트 생성!!");
            sessionImages = new ArrayList<>();
        }

        try{
            List<BoardImage_dto> client_ImageInfo= new ArrayList<>();
            //일단 신규 업로드 되는 파일은 잠시 임시 업로드 경로에 올렸다가 수정이 확정되면 정식 경로로 이동
            //클라이언트 상에는 제한된 정보만 제공
            for(int i=0;i<images.length;i++) {
                BoardImage_dto tempFile = BoardImage_dto.boardImageDto2(images[i]);
                tempFile.setBoardId(boardId);
                BoardImage_dto saveTempImageFile = boardImageService.saveTempImageFile(images[i], tempFile);

                sessionImages.add(saveTempImageFile);
                BoardImage_dto client=new BoardImage_dto();
                client.setName(saveTempImageFile.getName());
                client.setUUIDName(saveTempImageFile.getUUIDName());
                client_ImageInfo.add(client);
            }

            for(int i=0;i<sessionImages.size();i++) {
                BoardImage_dto dto= sessionImages.get(i);
                log.info("INDEX:{}, UUID : {}, Name : {}",i, dto.getUUID(), dto.getName());
            }

            session.setAttribute("UpdateImages", sessionImages);

            response.put("message", "Success");
            response.put("UpdateImages", client_ImageInfo);

        }catch(Exception e){
            log.error("Image Upload Error : {}", e.getMessage());
            response.put("message", "Fail");
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.ok(response);
    }

    //임시 등록된 이미지 삭제(전체) 만약 게시글 등록에서 해당 페이지를 벗어나면 동작
    @PostMapping("/delete")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteImage(HttpSession session) {
        log.info("임시 폴더내 파일들 삭제");
        Map<String, Object> response = new HashMap<>();
        List<BoardImage_dto> sessionImages = (List<BoardImage_dto>) session.getAttribute("tempImages");
        List<BoardImage_dto> sessionImages2 = (List<BoardImage_dto>) session.getAttribute("UpdateImages");

        //게시글 수정 화면에서 동작(실제로는 게시글 수정화면에서 이미지 삭제, 등록이 즉각적으로 반영되서 사용안 될듯)
        if( sessionImages2!=null && sessionImages==null){
            for(BoardImage_dto dto : sessionImages2) {
                boardImageService.deleteImageFile(dto.getUUID(), dto.getUUIDName(), dto.getBoardId());
            }
            if(sessionImages2.isEmpty()){
                session.removeAttribute("UpdateImages");
            }
            response.put("message", "Success");
        }
        //게시글 등록 화면에서 동작
        else if(sessionImages2==null && sessionImages!=null){
            for (BoardImage_dto dto : sessionImages) {
                boardImageService.deleteTempImageFile(dto.getUUID(), dto.getUUIDName());
            }
            if(sessionImages.isEmpty()){
                session.removeAttribute("tempImages");
            }
            response.put("message", "Success");
        }else{
            response.put("message", "No images");
        }

        return ResponseEntity.ok(response);
    }


    //임시 등록된 이미지 삭제(특정 하나)(세션을 통해 검증)

    @PostMapping("/deleteOne")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteOneImage(HttpSession session,
                                                              @RequestParam("ImageName") String ImageName,
                                                              @RequestParam("ImageUUIDName") String ImageUUIDName) {
        Map<String, Object> response = new HashMap<>();
        List<BoardImage_dto> sessionImages1 = (List<BoardImage_dto>) session.getAttribute("tempImages");
        List<BoardImage_dto> sessionImages2 = (List<BoardImage_dto>) session.getAttribute("UpdateImages");
        // 게시글 수정의 임시 업로드인 경우
        if (sessionImages1 == null && sessionImages2!=null) {
            log.info("게시글 수정의 임시 이미지 삭제");
            BoardImage_dto deleteDto = sessionImages2.stream()
                    .filter(dto -> dto.getName().equals(ImageName) && dto.getUUIDName().equals(ImageUUIDName))
                    .findFirst()
                    .orElse(null);
            if(deleteDto!=null){
                boardImageService.deleteTempImageFile(deleteDto.getUUID(), deleteDto.getUUIDName());
                sessionImages2.remove(deleteDto);
                if(sessionImages2.isEmpty()){
                    session.removeAttribute("UpdateImages");
                }
                response.put("message", "Success");
            }
        }
        // 게시글 입력의 임시 업로드인 경우
        else if(sessionImages1!=null){
            log.info("게시글 업로드의 임시 이미지 삭제");
            BoardImage_dto deleteDto = sessionImages1.stream()
                   .filter(dto -> dto.getName().equals(ImageName) && dto.getUUIDName().equals(ImageUUIDName))
                   .findFirst()
                   .orElse(null);

            if (deleteDto!= null) {
                boardImageService.deleteTempImageFile(deleteDto.getUUID(), deleteDto.getUUIDName());
                sessionImages1.remove(deleteDto);
                if(sessionImages1.isEmpty()){
                    session.removeAttribute("tempImages");
                }
                response.put("message", "Success");
            } else {
                response.put("message", "Not found");
            }
        }else{
            response.put("message","NO temp ImageFile");
        }
        return ResponseEntity.ok(response);
    }

    //특정 게시물의 이미지 하나 삭제(이미 등록된 것을 삭제)
    /*
        해당 게시물 내의 이미지가 맞는지, 실제 게시글 작성자가 맞는지 확인 필요(변조 방지)
        세션과 시큐리티 활용
    */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/deleteImage")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteImageByBoardId(HttpSession session,
                                                              @RequestParam("boardId") Long boardId,
                                                              @RequestParam("ImageUUID") String uuid,
                                                              @RequestParam("ImageUUIDName") String ImageUUIDName) {
        Map<String, Object> response = new HashMap<>();
        Board_dto boardDto=boardService.getBoard(boardId);
        UserDTO userDTO=boardSecurityService.getUser();
        if(boardDto.getUser_login_Id().compareTo(userDTO.getUserLoginId())!=0){
            response.put("message", "Not Authorized");
            return ResponseEntity.ok(response);
        }
        if(boardDto==null){
            response.put("message", "NOBoard");
            return ResponseEntity.ok(response);
        }
        //만약 게시물 내의 이미지가 아니거나 본인이 작성한 게시물이 아니면 에러화면 출력

        List<BoardImage_dto> sessionImages = (List<BoardImage_dto>) session.getAttribute("UpdateImages");
        if (sessionImages == null) {
            response.put("message", "No images");
            return ResponseEntity.ok(response);
        }
        boardImageService.deleteImageFile(uuid,ImageUUIDName,boardId);
        sessionImages.removeIf(dto -> dto.getUUID().equals(uuid) && dto.getUUIDName().equals(ImageUUIDName));
        if(sessionImages.isEmpty()){
            session.removeAttribute("UpdateImages");
        }
        session.setAttribute("UpdateImages", sessionImages);
        response.put("message", "Success");
        return ResponseEntity.ok(response);
    }


    //특정 게시물의 이미지 읽어오기
    @PermitAll
    @GetMapping("/images/{boardId}")
    @ResponseBody
    public ResponseEntity<List<BoardImage_dto>> getImagesByBoardId(@PathVariable Long boardId) {
        List<BoardImage_dto> images = boardImageService.getImagesByBoardId(boardId);
        return ResponseEntity.ok(images);
    }

    //모든 이미지 읽어오기(관리자 용)
    @PreAuthorize("hasRole('manager')")
    @GetMapping("/all")
    @ResponseBody
    public ResponseEntity<List<BoardImage_dto>> getAllImages() {
        List<BoardImage_dto> images = boardImageService.findAllImages();
        return ResponseEntity.ok(images);
    }
}
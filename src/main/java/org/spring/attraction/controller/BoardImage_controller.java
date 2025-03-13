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
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

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

    //이미지 임시 등록(세션에 이미지 dto를 저장)
    //한 사용자가 하나의 창만 이용해서 기능 수행하면 아무런 문제가 없음
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/TempSave")
    @ResponseBody
    public ResponseEntity<?> saveTempImage(@RequestParam("tempImage") MultipartFile tempImage, HttpSession session) {
        log.info("Temp Save Image");

        //파일 보안검사 필요

        try {
            BoardImage_dto tempFile = BoardImage_dto.boardImageDto2(tempImage);
            //실제로는 저장 안하도록 변경
            BoardImage_dto saveTempImageFile = boardImageService.saveTempImageFile(tempImage,tempFile);

            //세션의 기존 이미지 dto 리스트에 등록하거나, 새로운 이미지 dto 리스트 생성 후 세션에 등록
            List<BoardImage_dto> sessionImages = (List<BoardImage_dto>) session.getAttribute("tempImages");
            if (sessionImages == null) {
                sessionImages = new ArrayList<>();
            }
            sessionImages.add(saveTempImageFile);
            session.setAttribute("tempImages", sessionImages);

            String base64Image = Base64.getEncoder().encodeToString(tempImage.getBytes());
            Map<String, Object> jsonMeta = new HashMap<>();
            jsonMeta.put("ImageName", saveTempImageFile.getName());
            jsonMeta.put("ImageUUIDName",saveTempImageFile.getUUIDName());
            jsonMeta.put("message", "Success");
            jsonMeta.put("ImageBase64", base64Image);
            jsonMeta.put("imageContentType", tempImage.getContentType());

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(jsonMeta);

        }catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Fail");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    //이미지 등록 2(기존의 게시글 수정 화면에서 이용)(검증 필요)
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/ImageUpdates")
    @ResponseBody
    public ResponseEntity<?> saveImage(@RequestParam("boardId") Long boardId, MultipartFile[] images, HttpSession session) {
        log.info("게시글 내 새로운 이미지 임시 등록");
        UserDTO userDto=boardSecurityService.getUser();
        Map<String, Object> jsonMeta = new HashMap<>();
        Board_dto boardDto=boardService.getBoard(boardId);

        if(boardDto==null){
            jsonMeta.put("message", "NOBoard");
            return ResponseEntity.ok(jsonMeta);
        }
        if(boardDto.getUser_login_Id().compareTo(userDto.getUserLoginId())!=0){
            jsonMeta.put("message", "NOPermission");
            return ResponseEntity.ok(jsonMeta);
        }
        //이미지 수정 화면에 있던 기존 이미지가 저장된 세션 정보를 가져옴
        List<BoardImage_dto> sessionImages = (List<BoardImage_dto>) session.getAttribute("UpdateImages");

        if(sessionImages==null){
            log.info("세션에 등록할 리스트 생성!!");
            sessionImages = new ArrayList<>();
        }

        try{
            List<Map<String, Object>> client_ImageInfo= new ArrayList<>();
            //일단 신규 업로드 되는 파일은 잠시 세션 상에 정보로만 등록되었다가 수정이 확정되면 정식 경로로 이동
            //클라이언트 상에는 제한된 정보만 제공
            for(int i=0;i<images.length;i++) {
                MultipartFile file = images[i];
                String base64Image = Base64.getEncoder().encodeToString(images[i].getBytes());
                BoardImage_dto tempFile = BoardImage_dto.boardImageDto2(file);
                tempFile.setBoardId(boardId);
                BoardImage_dto savedTempImageFile = boardImageService.saveTempImageFile(file, tempFile);
                log.info("이름 {} UUID+이름 {} ",savedTempImageFile.getName(),savedTempImageFile.getUUIDName());

                sessionImages.add(savedTempImageFile);

                Map<String, Object> clientImage = new HashMap<>();
                clientImage.put("name", savedTempImageFile.getName());
                clientImage.put("uuidname", savedTempImageFile.getUUIDName());
                clientImage.put("ImageBase64", base64Image);
                clientImage.put("imageContentType", file.getContentType());

                client_ImageInfo.add(clientImage);
            }

            for(int i=0;i<sessionImages.size();i++) {
                BoardImage_dto dto= sessionImages.get(i);
                log.info("INDEX:{}, UUID : {}, Name : {}",i, dto.getUUID(), dto.getName());
            }

            session.setAttribute("UpdateImages", sessionImages);

            jsonMeta.put("message", "Success");
            jsonMeta.put("UpdateImages", client_ImageInfo);

        }catch(Exception e){
            log.error("Image Upload Error : {}", e.getMessage());
            jsonMeta.put("message", "Fail");
            return ResponseEntity.ok(jsonMeta);
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonMeta);
    }

    //임시 등록된 이미지 삭제(전체) 만약 게시글 등록에서 해당 페이지를 벗어나면 동작
    @PostMapping("/delete")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteImage(HttpSession session) {
        log.info("임시 폴더내 파일들 삭제");
        Map<String, Object> response = new HashMap<>();
        List<BoardImage_dto> sessionImages = (List<BoardImage_dto>) session.getAttribute("tempImages");
        List<BoardImage_dto> sessionImages2 = (List<BoardImage_dto>) session.getAttribute("UpdateImages");

        //게시글 수정 화면에서 동작
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
            session.removeAttribute("tempImages");
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
            sessionImages2.removeIf(dto -> dto.getName().equals(ImageName) && dto.getUUIDName().equals(ImageUUIDName));
            if(sessionImages2.isEmpty()){
                session.removeAttribute("UpdateImages");
            }
            response.put("message", "Success");
        }
        // 게시글 입력의 임시 업로드인 경우
        else if(sessionImages1!=null){
            log.info("게시글 업로드의 임시 이미지 삭제");
            sessionImages1.removeIf(dto -> dto.getName().equals(ImageName) && dto.getUUIDName().equals(ImageUUIDName));
            if(sessionImages1.isEmpty()) {
                session.removeAttribute("tempImages");
            }
            response.put("message", "Success");
        }else{
            response.put("message","NO temp ImageFile");
        }
        return ResponseEntity.ok(response);
    }

    //특정 게시물의 이미지 하나 삭제(이미 등록된 것을 삭제)
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

        List<BoardImage_dto> sessionImages = (List<BoardImage_dto>) session.getAttribute("CurrentImage");
        if (sessionImages == null) {
            response.put("message", "No images");
            return ResponseEntity.ok(response);
        }
        boardImageService.deleteImageFile(uuid,ImageUUIDName,boardId);
        sessionImages.removeIf(dto -> dto.getUUID().equals(uuid) && dto.getUUIDName().equals(ImageUUIDName));
        if(sessionImages.isEmpty()){
            session.removeAttribute("CurrentImage");
        }
        session.setAttribute("CurrentImage", sessionImages);
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
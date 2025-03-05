package org.spring.attraction.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.spring.attraction.dto.BoardImage_dto;
import org.spring.attraction.service.BoardImage_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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

    //이미지 임시 등록(파일 스토리지에 이미지 저장, DB 상에 이미지 정보 저장, 세션에 이미지 dto를 저장)
    //한 사용자가 하나의 창만 이용해서 기능 수행하면 아무런 문제가 없음
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
            response.put("message", "Success");

        }catch (Exception e) {
            e.printStackTrace();
            response.put("message","Fail");
        }
        return ResponseEntity.ok(response);
    }

    //임시 등록된 이미지 삭제(전체)
    @PostMapping("/delete")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteImage(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        List<BoardImage_dto> sessionImages = (List<BoardImage_dto>) session.getAttribute("tempImages");
        if (sessionImages == null) {
            response.put("message", "No temp images");
        }else {
            for (BoardImage_dto dto : sessionImages) {
                boardImageService.deleteTempImageFile(dto.getUUID(), dto.getUUIDName());
            }
            if(sessionImages.isEmpty()){
                session.removeAttribute("tempImages");
            }
            response.put("message", "Success");
        }

        return ResponseEntity.ok(response);
    }

    //임시 등록된 이미지 삭제(특정 하나)
    @PostMapping("/deleteOne")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteOneImage(HttpSession session,
                                                              @RequestParam("ImageName") String ImageName,
                                                              @RequestParam("ImageUUIDName") String ImageUUIDName) {
        Map<String, Object> response = new HashMap<>();
        List<BoardImage_dto> sessionImages = (List<BoardImage_dto>) session.getAttribute("tempImages");
        if (sessionImages == null) {
            response.put("message", "No temp images");
        } else {
            BoardImage_dto deleteDto = sessionImages.stream()
                   .filter(dto -> dto.getName().equals(ImageName) && dto.getUUIDName().equals(ImageUUIDName))
                   .findFirst()
                   .orElse(null);

            if (deleteDto!= null) {
                boardImageService.deleteTempImageFile(deleteDto.getUUID(), deleteDto.getUUIDName());
                sessionImages.remove(deleteDto);
                if(sessionImages.isEmpty()){
                    session.removeAttribute("tempImages");
                }
                response.put("message", "Success");
            } else {
                response.put("message", "Not found");
            }
        }
        return ResponseEntity.ok(response);
    }




    //특정 게시물의 이미지 읽어오기
    @GetMapping("/images/{boardId}")
    @ResponseBody
    public ResponseEntity<List<BoardImage_dto>> getImagesByBoardId(@PathVariable Long boardId) {
        List<BoardImage_dto> images = boardImageService.getImagesByBoardId(boardId);
        return ResponseEntity.ok(images);
    }

    //모든 이미지 읽어오기(관리자 용)
    @GetMapping("/all")
    @ResponseBody
    public ResponseEntity<List<BoardImage_dto>> getAllImages() {
        List<BoardImage_dto> images = boardImageService.findAllImages();
        return ResponseEntity.ok(images);
    }
}
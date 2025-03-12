package org.spring.attraction.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.spring.attraction.dto.BoardImage_dto;
import org.spring.attraction.entity.Board;
import org.spring.attraction.entity.BoardImage;
import org.spring.attraction.repository.BoardImage_repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;

@Service
@NoArgsConstructor
@AllArgsConstructor
@Log4j2
public class BoardImage_service {

    @Autowired
    public BoardImage_repository boardImageRepository;

    //나중에 구글 드라이브, AWS 등 외부 경로로 변경
    public final String UPLOAD_PATH = "C:/upload/board_images/";
    //모든 이미지 파일 읽기
    public List<BoardImage_dto> findAllImages() {
        List<BoardImage> images = boardImageRepository.findAll();
        return images.stream().map(BoardImage_dto::boardImageDto).collect(Collectors.toList());
    }


    //게시글 내 이미지 파일 임시 입력(따로 저장하는 것 없이 UUID 등 각종 고유 값을 생성해서 세팅하고 전달)
    public BoardImage_dto saveTempImageFile(MultipartFile temp,BoardImage_dto tempFile){

        BoardImage boardImage = boardImage(tempFile,null);
        String uuidString = UUID.randomUUID().toString();
        String uuidName=uuidString+"_" + tempFile.getName();

        boardImage.setUUID(uuidString);
        boardImage.setUUIDName(uuidName);


        BoardImage_dto UpdateDTO=BoardImage_dto.boardImageDto(boardImage);
        UpdateDTO.setImg(temp);

        return UpdateDTO;
    }

    //게시글 내 이미지 파일 입력(DTO 내 MultipartFile 정보를 활용하여 직접 저장)
    public boolean saveImageFile(Board board,BoardImage_dto boardImageDto,MultipartFile multipartFile){

        String realUploadPath = UPLOAD_PATH + boardImageDto.getUUIDName();
        MultipartFile uploadFile=multipartFile;
        log.info("real upload path: " + realUploadPath);
        File file = new File(realUploadPath);

        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if(!file.exists()) {
                Files.copy(uploadFile.getInputStream(), Path.of(realUploadPath));
            }
        }catch(Exception e) {
            e.printStackTrace();
            log.error("파일 저장 실패: {}", realUploadPath);
            return false;
        }

        BoardImage boardImage=boardImage(boardImageDto, board);
        try {
            boardImage.setBoard(board);
            boardImage.setImagePath(realUploadPath);
            boardImage.setIsTemporary(false);
            boardImageRepository.save(boardImage);

        }catch (Exception e) {
            e.printStackTrace();
            deleteImageFile(boardImage.getUUID(), boardImage.getUUIDName(),board.getId());
            file.delete();
            log.error("DB 내 BoardImage 정보 저장실패");
            return false;
        }

        return true;
    }

    //임시 이미지 파일 읽기
    public BoardImage_dto getTempImages(String uuid, String uuidName){
        BoardImage boardImage = boardImageRepository.findBoardImageByUUIDAndUUIDNameAndBoardId(uuid, uuidName, null);
        return BoardImage_dto.boardImageDto(boardImage);
    }

    //이미지 파일 읽기(게시글 내 이미지 전부 읽기)
    public List<BoardImage_dto> getImagesByBoardId(Long boardId) {
        List<BoardImage> images = boardImageRepository.findAllByBoard_Id(boardId);
        return images.stream().map(BoardImage_dto::boardImageDto).collect(Collectors.toList());
    }


    //이미지 파일 삭제(이미 게시글 내 등록된 이미지 하나 삭제)
    @Transactional
    public void deleteImageFile(String uuid, String uuidName, Long boardId) {
        log.info("{}, {}, {}", uuid, uuidName, boardId);
        BoardImage boardImage = boardImageRepository.findBoardImageByUUIDAndUUIDNameAndBoardId(uuid, uuidName, boardId);

        String FilePath=boardImage.getImagePath();

        log.info("삭제할 정식 BoardImage: 게시글 아이디{}, 아이디 {}, UUID 이름 {}, 실제 경로 {}",
                boardImage.getBoard().getId(), boardImage.getId(),boardImage.getUUIDName(),
                boardImage.getImagePath());

        try {
            File deleteFile = new File(FilePath);
            deleteFile.delete();
        }catch (Exception e) {
            e.printStackTrace();
            log.error("파일 삭제 실패: {}", FilePath);
        }

        if((boardImage!=null)&& (!boardImage.getIsTemporary()) && (boardImage.getBoard().getId().equals(boardId))){
            boardImageRepository.deleteByUUIDAndUUIDName(uuid, uuidName);
        }
    }

    //이미지 파일 삭제(게시글 내 모든 이미지 삭제)
    public void deleteAllImageFile(Long boardId){
        List<BoardImage> boardImageList=boardImageRepository.findAllByBoard_Id(boardId);
        for(BoardImage boardImage: boardImageList){
            String FilePath=boardImage.getImagePath();

            try {
                File deleteFile = new File(FilePath);
                if (deleteFile.exists()) {
                    deleteFile.delete();
                }
            }catch (Exception e) {
                e.printStackTrace();
                log.error("파일 삭제 실패: {}", FilePath);
            }

        }
        boardImageRepository.deleteByBoard_Id(boardId);
    }

    public static BoardImage boardImage(BoardImage_dto boardImageDto,Board board){
        BoardImage boardImage = new BoardImage();
        boardImage.setId(boardImageDto.getId());
        boardImage.setName(boardImageDto.getName());
        boardImage.setUUID(boardImageDto.getUUID());
        boardImage.setUUIDName(boardImageDto.getUUIDName());
        boardImage.setImageSize(boardImageDto.getImageSize());
        boardImage.setImagePath(boardImageDto.getImagePath());
        boardImage.setBoard(board);
        boardImage.setIsTemporary(boardImageDto.getIsTemporary());
        return boardImage;
    }
}

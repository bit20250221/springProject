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
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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
    public final String TEMP_UPLOAD_PATH = "C:/upload/temp_board_images/";
    //모든 이미지 파일 읽기
    public List<BoardImage_dto> findAllImages() {
        List<BoardImage> images = boardImageRepository.findAll();
        return images.stream().map(BoardImage_dto::boardImageDto).collect(Collectors.toList());
    }


    //게시글 내 이미지 파일 임시 입력
    public BoardImage_dto saveTempImageFile(MultipartFile temp,BoardImage_dto tempFile){

        BoardImage boardImage = boardImage(tempFile,null);
        String uuidString = UUID.randomUUID().toString();
        String uuidName=uuidString+"_" + tempFile.getName();
        String realUploadPath=TEMP_UPLOAD_PATH + uuidName;
        try {
            File file = new File(realUploadPath);
            file.getParentFile().mkdirs();
            temp.transferTo(file);
        }catch (Exception e) {
            e.printStackTrace();
            log.error("파일 저장 실패: {}", realUploadPath);
        }

        boardImage.setUUID(uuidString);
        boardImage.setUUIDName(uuidName);
        boardImage.setImagePath(realUploadPath);

        boardImageRepository.save(boardImage);

        return BoardImage_dto.boardImageDto(boardImage);
    }

    //게시글 내 이미지 파일 입력(저장경로 수정, db 내 정보 수정)
    public boolean saveImageFile(BoardImage_dto boardImageDto, Board board){

        String realUploadPath = UPLOAD_PATH + boardImageDto.getUUIDName();
        String tempOldPath=TEMP_UPLOAD_PATH + boardImageDto.getUUIDName();
        log.info("temp old path: " + tempOldPath);
        log.info("real upload path: " + realUploadPath);
        File tempFile = new File(tempOldPath);
        File file = new File(realUploadPath);

        if(!tempFile.exists()){
            log.error("임시 파일이 존재하지 않습니다: {}", tempOldPath);
            return false;
        }

        try {
            file.getParentFile().mkdirs();
            log.info("파일 이동 진행: {} -> {}",tempFile.toPath(), file.toPath());
            Files.move(tempFile.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }catch (Exception e) {
            e.printStackTrace();
            tempFile.delete();
            log.error("파일 이동 실패: {} -> {}", tempOldPath, realUploadPath);
            return false;
        }

        BoardImage boardImage = boardImageRepository.findBoardImageByUUIDAndUUIDName(boardImageDto.getUUID(), boardImageDto.getUUIDName());

        if(boardImage==null){
            log.error("DB 내 BoardImage 찾기 실패: uuid={}, uuidName={}", boardImageDto.getUUID(), boardImageDto.getUUIDName());
            return false;
        }

        try {
            boardImage.setBoard(board);
            boardImage.setImagePath(realUploadPath);
            boardImage.setIsTemporary(false);
            boardImageRepository.save(boardImage);

        }catch (Exception e) {
            e.printStackTrace();
            deleteTempImageFile(boardImage.getUUID(), boardImage.getUUIDName());
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

    //임시 이미지 파일 삭제(한 개만 삭제)
    @Transactional
    public void deleteTempImageFile(String uuid, String uuidName) {
        BoardImage boardImage = boardImageRepository.findBoardImageByUUIDAndUUIDName(uuid, uuidName);
        String FilePath = boardImage.getImagePath();

        log.info("삭제할 임시 BoardImage: 아이디 {}, UUID 이름 {}, 실제 경로 {}",
                boardImage.getId(),boardImage.getUUIDName(),boardImage.getImagePath());

        try {
            File deleteFile = new File(FilePath);
            deleteFile.delete();
        }catch (Exception e) {
            e.printStackTrace();
            log.error("파일 삭제 실패: {}", FilePath);
        }

        if(boardImage!= null && boardImage.getIsTemporary()){
            boardImageRepository.deleteByUUIDAndUUIDName(uuid, uuidName);
        }
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

package org.spring.attraction.BoardAndCommentTest;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.spring.attraction.dto.BoardImage_dto;
import org.spring.attraction.repository.Board_repository;
import org.spring.attraction.service.BoardImage_service;
import org.spring.attraction.service.Board_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static javax.swing.UIManager.get;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class BoardImageTest {

    @Autowired
    private BoardImage_service boardImageService;

    @Autowired
    private Board_repository boardRepository;

    @Autowired
    private Board_service boardService;

    //나중에 구글 드라이브, AWS 등 외부 경로로 변경
    public final String UPLOAD_PATH = "C:/upload/board_images/";
    public final String TEMP_UPLOAD_PATH = "C:/upload/temp_board_images/";

    @Test
    public void boardImageTest() {
        //이미지 임시 업로드, 정식 업로드, 전체 읽어오기, 특정 게시물 이미지 읽어오기, 임시 이미지 삭제, 정식 이미지 삭제
        //ImageRegisterTest();
        //ImageTempDeleteTest();
        //ImageDeleteTest();
        //ImageReadTest();
    }


    //임시등록 이미지 파일 읽기 테스트(테스트 완료)
    public void ImageReadTest(){

        //임시 업로드, 실제 파일
        File file = new File("src/main/resources/zoom_structure.png");
        FileInputStream fis;
        MultipartFile mockMultipartFile;
        try {
            fis = new FileInputStream(file);
            mockMultipartFile = new MockMultipartFile("tempImage", file.getName(), "image/png", fis);
            BoardImage_dto imageDto = BoardImage_dto.boardImageDto2(mockMultipartFile);

            //나중에는 이 dto 값을 클라이언트에서 읽어온다.
            BoardImage_dto savedDto= boardImageService.saveTempImageFile(mockMultipartFile, imageDto);

            //등록된 임시파일을 읽어온다.
            BoardImage_dto readDto=boardImageService.getTempImages(savedDto.getUUID(), savedDto.getUUIDName());

            assertEquals(savedDto.getUUID(), readDto.getUUID());
            assertEquals(savedDto.getName(), readDto.getName());
            assertEquals(savedDto.getImagePath(), readDto.getImagePath());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //임시 등록 및 정식 등록 테스트(테스트 완료)
    public void ImageRegisterTest(){
        // Arrange

        //임시 업로드, 실제 파일
        File file = new File("src/main/resources/zoom_structure.png");
        FileInputStream fis;
        MultipartFile mockMultipartFile;
        try {
            fis = new FileInputStream(file);
            mockMultipartFile = new MockMultipartFile("tempImage", file.getName(), "image/png", fis);
            BoardImage_dto imageDto = BoardImage_dto.boardImageDto2(mockMultipartFile);

            //나중에는 이 dto 값을 클라이언트에서 읽어온다.
            BoardImage_dto savedDto= boardImageService.saveTempImageFile(mockMultipartFile, imageDto);

            //정식 등록
            //boardImageService.saveImageFile(savedDto, boardRepository.findById(1L).get());
            BoardImage_dto readImage =boardImageService.getImagesByBoardId(1L).get(0);

            // Assert
            assertEquals(savedDto.getUUID(), readImage.getUUID());
            assertEquals(savedDto.getName(), readImage.getName());
            assertEquals(savedDto.getImagePath(), readImage.getImagePath());


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //임시 업로드 테스트
    public void ImageTempUploadTest() {
        // Arrange
        MultipartFile mockMultipartFile = Mockito.mock(MultipartFile.class);
        Mockito.when(mockMultipartFile.getOriginalFilename()).thenReturn("testImage.jpg");
        BoardImage_dto tempFile = new BoardImage_dto();
        tempFile.setName("testImage.jpg");

        // Simulate file transfer failure
        try {
            Mockito.doThrow(new IOException("File transfer failed")).when(mockMultipartFile).transferTo(Mockito.any(File.class));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Act & Assert
        Exception exception = assertThrows(IOException.class, () -> {
            boardImageService.saveTempImageFile(mockMultipartFile, tempFile);
        });

        assertEquals("File transfer failed", exception.getMessage());
    }

    //임시 파일 삭제 테스트(테스트 완료(임시 저장된 정보 출력하고 db와 폴더에 없는 것을 확인))
    public void ImageTempDeleteTest() {
        File file = new File("src/main/resources/zoom_structure.png");
        FileInputStream fis;
        MultipartFile mockMultipartFile;

        try {

            //임시 업로드
            fis = new FileInputStream(file);
            mockMultipartFile = new MockMultipartFile("tempImage", file.getName(), "image/png", fis);
            BoardImage_dto imageDto = BoardImage_dto.boardImageDto2(mockMultipartFile);

            //나중에는 이 dto 값을 클라이언트에서 읽어온다.
            BoardImage_dto savedDto = boardImageService.saveTempImageFile(mockMultipartFile, imageDto);

            //boardImageService.deleteTempImageFile(savedDto.getUUID(),savedDto.getUUIDName());

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    //정식파일 삭제 테스트(테스트 완료(정식 등록된 정보 출력하고 db와 폴더에 없는 것을 확인))
    public void ImageDeleteTest() {
        File file = new File("src/main/resources/zoom_structure.png");
        FileInputStream fis;
        MultipartFile mockMultipartFile;

        try {

            //임시 업로드
            fis = new FileInputStream(file);
            mockMultipartFile = new MockMultipartFile("tempImage", file.getName(), "image/png", fis);
            BoardImage_dto imageDto = BoardImage_dto.boardImageDto2(mockMultipartFile);

            //나중에는 이 dto 값을 클라이언트에서 읽어온다.
            BoardImage_dto savedDto = boardImageService.saveTempImageFile(mockMultipartFile, imageDto);

            //정식 업로드
            //boardImageService.saveImageFile(savedDto, boardRepository.findById(1L).get());

            //정식 파일 삭제
            boardImageService.deleteImageFile(savedDto.getUUID(), savedDto.getUUIDName(),1L);

        }catch (Exception e){
            e.printStackTrace();
        }
    }


}

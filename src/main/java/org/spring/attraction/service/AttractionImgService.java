package org.spring.attraction.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.spring.attraction.ENUM.AttractionMessage;
import org.spring.attraction.dto.AttractionImgDto;
import org.spring.attraction.entity.AttractionImg;
import org.spring.attraction.repository.AttractionImgRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttractionImgService {
    private final AttractionImgRepository attractionImgRepository;


    public AttractionImgDto findByAttractionId(Long id) {
        AttractionImg attractionImg = attractionImgRepository.findByAttractionId(id).orElse(null);
        if(attractionImg == null) {
            return null;
        }
        return AttractionImgDto.toDto(attractionImg);
    }

    public AttractionImg save(MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(AttractionImgDto.IMG_DIR_URL);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        String fileUUID = UUID.randomUUID().toString();
        String fileName = multipartFile.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileUUID + "_" + fileName);

        Files.copy(multipartFile.getInputStream(), filePath);

        AttractionImg attractionImg = new AttractionImg();
        attractionImg.setName(fileName);
        attractionImg.setUUID(fileUUID);

        return attractionImg;
    }

    public AttractionMessage update(AttractionImgDto attractionImgDto) {
        try{
            Path uploadPath = Paths.get(AttractionImgDto.IMG_DIR_URL);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            AttractionImg attractionImg = attractionImgRepository.findByAttractionId(attractionImgDto.getAttractionId()).orElse(null);
            if(attractionImg == null) {
                return AttractionMessage.getTypeById(-14);
            }

            Path deleteFilePath = Paths.get(AttractionImgDto.IMG_DIR_URL).resolve(attractionImg.getUUID() + "_" + attractionImg.getName()).normalize();
            if (!Files.exists(deleteFilePath)) {
                return AttractionMessage.getTypeById(-12);
            }
            Files.delete(deleteFilePath);

            String fileUUID = UUID.randomUUID().toString();
            String fileName = attractionImgDto.getImg().getOriginalFilename();
            Path saveFilePath = uploadPath.resolve(fileUUID + "_" + fileName);

            Files.copy(attractionImgDto.getImg().getInputStream(), saveFilePath);

            attractionImg.setName(fileName);
            attractionImg.setUUID(fileUUID);

        }catch (Exception e){
            e.printStackTrace();
            return AttractionMessage.getTypeById(-13);
        }

        return null;
    }

    public AttractionMessage delete(Long attractionId) {
        try{
            AttractionImg attractionImg = attractionImgRepository.findByAttractionId(attractionId).orElse(null);
            if(attractionImg == null) {
                return AttractionMessage.getTypeById(-14);
            }
            Path deleteFilePath = Paths.get(AttractionImgDto.IMG_DIR_URL).resolve(attractionImg.getUUID() + "_" + attractionImg.getName()).normalize();
            if (!Files.exists(deleteFilePath)) {
                return AttractionMessage.getTypeById(-12);
            }
            Files.delete(deleteFilePath);
            attractionImgRepository.delete(attractionImg);

        }catch (Exception e){
            e.printStackTrace();
            return AttractionMessage.getTypeById(-13);
        }
        return null;
    }
}

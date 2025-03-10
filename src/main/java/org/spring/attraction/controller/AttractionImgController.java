package org.spring.attraction.controller;

import lombok.RequiredArgsConstructor;
import org.spring.attraction.dto.AttractionImgDto;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequiredArgsConstructor
public class AttractionImgController {

    @GetMapping("/files/{filename}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(AttractionImgDto.IMG_DIR_URL).resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                Path defaultFilePath = Paths.get(AttractionImgDto.IMG_DIR_URL).resolve("default.png").normalize();
                Resource defaultResource = new UrlResource(defaultFilePath.toUri());

                if (!defaultResource.exists() || !defaultResource.isReadable()) {
                    return ResponseEntity.notFound().build();
                }

                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_PNG)
                        .body(defaultResource);
            }

            String encodedFileName = URLEncoder.encode(filename, StandardCharsets.UTF_8);


            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // 필요하면 변경 가능
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename*=UTF-8''" + encodedFileName)
                    .body(resource);

        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }

    }
}

package org.spring.attraction.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Configuration
public class FileConfig  implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        /*
            임시 이미지 저장을 위한 설정
            - /resources/upload/** : /upload/temp_board_images/ 경로로 매핑
            - file:///C:/upload/temp_board_images/ : 실제 이미지가 저장되는 경로
            - resourceChain(true) : Spring Boot 2.3.0 이상부터 resource handler chain 지원, URL decoding 적용
            - addResolver(new PathResourceResolver()) : 커스텀 PathResourceResolver를 등록
            - getResource(resourcePath, location) : resourcePath를 URLDecoder를 이용해 decoding, location에 실제 경로 전달
            - super.getResource(decodedPath, location) : 디코딩된 경로를 기준으로 실제 파일 리소스를 찾는다
        */

        registry.addResourceHandler("/resources/upload/**")
               .addResourceLocations("file:///C:/upload/board_images/").resourceChain(true)
                .addResolver(new PathResourceResolver() {
                    @Override
                    protected Resource getResource(String resourcePath, Resource location) throws IOException {
                        String decodedPath = URLDecoder.decode(resourcePath, StandardCharsets.UTF_8);
                        return super.getResource(decodedPath, location);
                    }
                });
    }
}

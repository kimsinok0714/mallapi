package com.example.mallapi.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnailator;

@Component
@Slf4j
public class CustomFileUtil {

    @Value("${org.zerock.upload.path}")
    private String uploadPath;


    @PostConstruct
    public void init() {
        // 폴더 생성
        File tempFolder = new File(uploadPath);
        if (!tempFolder.exists()) {
            tempFolder.mkdir();
        }

        uploadPath = tempFolder.getAbsolutePath();
        log.info("file uploadPath : {}", uploadPath);

    }


    public List<String> saveFiles(List<MultipartFile> files) throws RuntimeException {
        
        if (files == null || files.size() == 0) {
            return null;
        }

        List<String> uploadFileNames = new ArrayList<>();

        for(MultipartFile file : files) {
            // uuid : 32 자리
            String savedFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

            // 파일 시스템의 경로를 나타내며, 파일 입출력 작업을 위해 사용된다.
            Path savePath = Paths.get(uploadPath, savedFileName);

            try {
                Files.copy(file.getInputStream(), savePath);    

                String contentType = file.getContentType();

                // 썸네일 처리
                if (contentType != null || contentType.startsWith("image")) {
                    Path thumnailPath = Paths.get(uploadPath, "s_" + savedFileName);
                    Thumbnailator.createThumbnail(savePath.toFile(), thumnailPath.toFile(), 200, 200);
                }

                uploadFileNames.add(savedFileName);

            } catch (Exception e) {
                throw new RuntimeException();
            }
        }
        
        return uploadFileNames;        
    }


    public ResponseEntity<Resource> getFile(String fileName) {
        Resource resource = new FileSystemResource(uploadPath + File.separator + fileName);

        if (!resource.isReadable()) {
            resource = new FileSystemResource(uploadPath + File.separator + "default.jpg");
            
        }

        HttpHeaders headers = new HttpHeaders();
        try {            
            headers.add("content-type", Files.probeContentType(resource.getFile().toPath()));
    
        } catch (Exception e) {
            throw new RuntimeException();
        }

        return ResponseEntity.ok().headers(headers).body(resource);
        
    }


    public void deleteFile(List<String> fileNames) {
        if (fileNames == null || fileNames.size() == 0) {
            return;
        }

        fileNames.forEach(fileName -> {
            // 썸네일 이미지 제거
            String thumbnailFileName = "s_" + fileName;
            
            Path thumbnailPath = Paths.get(uploadPath, thumbnailFileName);
            Path filePath = Paths.get(uploadPath, fileName);

            try {
                Files.deleteIfExists(thumbnailPath);
                Files.deleteIfExists(filePath);
            } catch (IOException e){
                throw new RuntimeException();
            }            

        });
    }

}

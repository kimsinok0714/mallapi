package com.example.mallapi.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.mallapi.dto.PageRequestDto;
import com.example.mallapi.dto.PageResponseDto;
import com.example.mallapi.dto.ProductDto;
import com.example.mallapi.service.ProductService;
import com.example.mallapi.util.CustomFileUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;



@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    private final CustomFileUtil fileUtil;
    
   
    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFile(@PathVariable("fileName") String fileName) {        
        return fileUtil.getFile(fileName);
    }


    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/list")
    public PageResponseDto<ProductDto> list(PageRequestDto pageRequestDto) {
        PageResponseDto<ProductDto>  result = productService.getList(pageRequestDto);
        log.info("PageRequestDto params : {}", pageRequestDto);
        log.info("PageResponseDto<ProductDto> result : {}", result);
        return result;
    } 


    @PostMapping("/")
    public Map<String, Long> register(ProductDto productDto) {
        
        List<MultipartFile> files = productDto.getFiles();
        List<String> uploadFileNames = fileUtil.saveFiles(files);

        productDto.setUploadFileNames(uploadFileNames);

        Long pno = productService.register(productDto);

        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            new RuntimeException();
        }
        
        return Map.of("result", pno);
    }


    @GetMapping("/read/{pno}")
    public ProductDto read(@PathVariable("pno") Long pno) {
        return productService.get(pno);
    }
     
    
    @PutMapping("/{pno}")
    public Map<String, String> modify(@PathVariable("pno") Long pno, ProductDto productDto) {

        productDto.setPno(pno);

        ProductDto olProductDto = productService.get(pno);

        // File Upload
        List<MultipartFile> files = productDto.getFiles();
        List<String> currentUploadFileNames = fileUtil.saveFiles(files);

        // Keep files
        List<String> uploadedFileNames = productDto.getUploadFileNames();

        if (currentUploadFileNames != null && !uploadedFileNames.isEmpty()) {
            uploadedFileNames.addAll(currentUploadFileNames);
        }

        productService.modify(olProductDto);

        List<String> oldFileNames = olProductDto.getUploadFileNames();
        if (oldFileNames != null && !oldFileNames.isEmpty()) {
            List<String> removeFiles = oldFileNames.stream().filter(fileName -> uploadedFileNames.indexOf(fileName) == -1).collect(Collectors.toList());
            fileUtil.deleteFile(removeFiles);
        }

        return Map.of("RESULT", "SUCCESS");
    }


    @DeleteMapping("/{pno}")
    public Map<String, String> remove(@PathVariable Long pno) {
        
        List<String> oldFileNames = productService.get(pno).getUploadFileNames();
        
        productService.remove(pno);    

        fileUtil.deleteFile(oldFileNames);

        return Map.of("RESULT", "SUCCESS");

    }


}

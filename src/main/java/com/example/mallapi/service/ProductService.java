package com.example.mallapi.service;

import org.springframework.transaction.annotation.Transactional;

import com.example.mallapi.dto.PageRequestDto;
import com.example.mallapi.dto.PageResponseDto;
import com.example.mallapi.dto.ProductDto;

@Transactional
public interface ProductService {

    PageResponseDto<ProductDto> getList(PageRequestDto pageRequestDto);

    long register(ProductDto productDto);

    ProductDto get(Long pno);
    
    void modify(ProductDto productDto);

    void remove(Long pno);
 
}

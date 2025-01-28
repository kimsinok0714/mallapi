package com.example.mallapi.repository.search;

import com.example.mallapi.dto.PageRequestDto;
import com.example.mallapi.dto.PageResponseDto;
import com.example.mallapi.dto.ProductDto;

public interface ProductSearch {

    PageResponseDto<ProductDto> searchList(PageRequestDto pageRequestDto);

}

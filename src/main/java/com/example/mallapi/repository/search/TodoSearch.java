package com.example.mallapi.repository.search;

import org.springframework.data.domain.Page;

import com.example.mallapi.domain.Todo;
import com.example.mallapi.dto.PageRequestDto;

public interface TodoSearch {

    Page<Todo> search(PageRequestDto pageRequestDto); 

}

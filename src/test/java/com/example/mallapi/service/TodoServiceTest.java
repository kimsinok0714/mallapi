package com.example.mallapi.service;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.mallapi.dto.PageRequestDto;
import com.example.mallapi.dto.TodoDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class TodoServiceTest {

    @Autowired
    private TodoService todoService;

    @Test
    public void test1() {

        log.info("todo : {}" , todoService.get(1L));

    }


    
    @Test
    public void testRegister() {    
        
        TodoDto todoDto = TodoDto.builder()
            .title("Title")
            .content("Content")
            .dueDate(LocalDate.of(2024, 12, 1))
            .build();
            
        Long tno = todoService.register(todoDto);

        log.info("tno : {}", tno);

    }


    @Test
    public void testGetList() {
        
        PageRequestDto pageRequestDto = PageRequestDto.builder().build();
        //PageRequestDto pageRequestDto = PageRequestDto.builder().page(11).build();

        log.info("{}", todoService.getList(pageRequestDto));



    }

}

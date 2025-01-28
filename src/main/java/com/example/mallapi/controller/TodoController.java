package com.example.mallapi.controller;

import java.util.Map;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.service.annotation.PutExchange;
import com.example.mallapi.dto.PageRequestDto;
import com.example.mallapi.dto.PageResponseDto;
import com.example.mallapi.dto.TodoDto;
import com.example.mallapi.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/todoes")
public class TodoController {

    public final TodoService todoService;

    
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    // Path Variable
    @GetMapping("/{tno}")
    public TodoDto get(@PathVariable("tno") Long tno) {
        return todoService.get(tno);
    }

    // QueryString : 상황에 따라 다른 결과
        
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/list")
    public PageResponseDto<TodoDto> list(PageRequestDto pageRequestDto) {
        log.info("pageRequestDto : {}", pageRequestDto);
        log.info("todoService.getList : {}", todoService.getList(pageRequestDto));
        return todoService.getList(pageRequestDto);        
    }


    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping("/")
    public Map<String, Long> register(@RequestBody TodoDto dto) {

        log.info("dueDate : {}", dto.getDueDate());
        
        Long tno = todoService.register(dto);

        return Map.of("TNO", tno);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PutExchange("/{tno}")
    public Map<String, String> modify(@PathVariable("tno") Long tno, @RequestBody TodoDto todoDto) {

        todoDto.setTno(tno);
        todoService.modify(todoDto);

        return Map.of("RESULT", "SUCCESS");
    }
    

    @DeleteMapping("/{tno}")
    public Map<String, String> remove(@PathVariable Long tno) {
        todoService.remove(tno);

        return Map.of("RESULT", "SUCCESS");
    }


}

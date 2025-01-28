package com.example.mallapi.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.example.mallapi.domain.Todo;
import com.example.mallapi.dto.PageRequestDto;
import com.example.mallapi.dto.PageResponseDto;
import com.example.mallapi.dto.TodoDto;
import com.example.mallapi.repository.TodoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {
    
    private final TodoRepository todoRepository;

    
    @Override
    public TodoDto get(Long tno) {        
        Optional<Todo> result = todoRepository.findById(tno);
        Todo todo = result.orElseThrow();
        return this.entityToDto(todo);            
    }

    
    @Override
    public Long register(TodoDto dto) {
        Todo todo = this.dtoToEntity(dto);
        Todo result = todoRepository.save(todo);
        return result.getTno();
    }


    @Override
    public void modify(TodoDto todoDto) {
        Optional<Todo> result = todoRepository.findById(todoDto.getTno());

        Todo todo = result.orElseThrow();

        todo.changeTitle(todoDto.getTitle());
        todo.changeContent(todoDto.getContent());
        todo.changeDueDate(todoDto.getDueDate());
        todo.changeComplete(todoDto.isComplete());

        todoRepository.save(todo);
        
    }

    @Override
    public void remove(Long tno) {
       todoRepository.deleteById(tno);        
    }


    @Override
    public PageResponseDto<TodoDto> getList(PageRequestDto pageRequestDto) {
    
        Page<Todo> result = todoRepository.search(pageRequestDto);
        
        // Todo List => TodoDto List

        List<TodoDto> todoList = result.get().map(todo -> entityToDto(todo)).collect(Collectors.toList());

        PageResponseDto responseDto = PageResponseDto.<TodoDto>withAll()
                .dtoList(todoList)
                .pageRequestDto(pageRequestDto)
                .totalCount(result.getTotalElements())
                .build();

        return responseDto;
    }

}

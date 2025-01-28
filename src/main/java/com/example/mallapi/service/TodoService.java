package com.example.mallapi.service;

import org.springframework.transaction.annotation.Transactional;

import com.example.mallapi.domain.Todo;
import com.example.mallapi.dto.PageRequestDto;
import com.example.mallapi.dto.PageResponseDto;
import com.example.mallapi.dto.TodoDto;


@Transactional
public interface TodoService {

    PageResponseDto<TodoDto> getList(PageRequestDto pageRequestDto);

    TodoDto get(Long tno);

    Long register(TodoDto todoDto);

    void modify(TodoDto todoDto);

    void remove(Long tno);

    default TodoDto entityToDto(Todo todo) {

        TodoDto todoDto =
            TodoDto.builder()
                .tno(todo.getTno())
                .title(todo.getTitle())
                .content(todo.getContent())
                .dueDate(todo.getDueDate())
                .complete(todo.isComplete())
                .build();

        return todoDto;

    }


    default Todo dtoToEntity(TodoDto todoDto) {
        
        Todo todo =
            Todo.builder()
                .tno(todoDto.getTno())
                .title(todoDto.getTitle())
                .content(todoDto.getContent())
                .dueDate(todoDto.getDueDate())
                .complete(todoDto.isComplete())
                .build();

        return todo;

    }

}

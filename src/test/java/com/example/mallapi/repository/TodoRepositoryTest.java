package com.example.mallapi.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.example.mallapi.domain.Todo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class TodoRepositoryTest {

    @Autowired
    private TodoRepository todoRepository;

    @Test
    public void test() {
        Assertions.assertNotNull(todoRepository);
        log.info("call");
        
    }

    @Test
    public void testInsert() {
        Todo todo = Todo.builder()
            .title("제목")
            .content("내용")
            .dueDate(LocalDate.of(2024, 11, 12))
            .complete(false)
            .build();
        
            Todo savedTodo = todoRepository.save(todo);
            log.info("savedTodo : {}", savedTodo);
    }

    @Test
    public void testRead() throws Exception {

        Optional<Todo> result = todoRepository.findById(1L);
        
        if (result.isPresent()) {
            log.info("result : {}", result);            
        } else {
            throw new Exception("Todo item not found with id : 1");
        }
    }


    @Test
    public void testUpdate() {

        Optional<Todo> result = todoRepository.findById(1L);

        if (result.isPresent()) {
            Todo todo = result.get();
            todo.changeTitle("제목 변경");
            todo.changeContent("내용 변경");
            todoRepository.save(todo);
        }


    }

    @Test
    public void testPaging() {

        Pageable pageable = PageRequest.of(0, 10, Sort.by("tno").descending());
        
        Page<Todo> result = todoRepository.findAll(pageable);

        //log.info("data : {}", result.getContent());

        result.getContent().stream().forEach(todo -> log.info("todo : {}", todo));

    }


    // @Test 
    // public void testSearch1() {
    //     todoRepository.search1();
    // }

}

















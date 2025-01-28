package com.example.mallapi.repository.search;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import com.example.mallapi.domain.QTodo;
import com.example.mallapi.domain.Todo;
import com.example.mallapi.dto.PageRequestDto;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;

/*
 * Dynamic Search Using QueryDSL
 */

@Slf4j
public class TodoSearchImpl extends QuerydslRepositorySupport implements TodoSearch {

    public TodoSearchImpl() {
        super(Todo.class);
    }    


    @Override
    public Page<Todo> search(PageRequestDto pageRequestDto) {
       
        QTodo todo = QTodo.todo;

        // JPQLQuery<Todo> query  = from(todo)
        //         .where(todo.title.contains("제목"));

        JPQLQuery<Todo> query = from(todo);

        /*
        JPQLQuery<Tuple> query = from(todo)
            .select(Projections.tuple(todo.id, todo.title))
            .where(todo.complete.eq(false));
        */


        // Paging in Spring Data JPA
        // PageRequest.of(pageNumber, pageSize, sort)
        // pageNumber : 0 , The first page number starts from 0
        // pageSize : The number of items to display per page
        // Sorting criteria : Sort in descending order based on tno
        // Pageable : Interface Including Paging and Sorting Information

        Pageable pageable =  PageRequest.of(
                pageRequestDto.getPage() - 1, 
                pageRequestDto.getSize(), 
                Sort.by("tno").descending());


        // pageable에 지정된 페이지 번호, 페이지 크기, 정렬 정보 등을 Querydsl 쿼리에 추가합니다.
        JPQLQuery<Todo> paginatedQuery = getQuerydsl().applyPagination(pageable, query);

        List<Todo> list = paginatedQuery.fetch();

        long count = paginatedQuery.fetchCount();
 
        // return PageableExecutionUtils.getPage(content, pageable, paginatedQuery::fetchCount);
        return new PageImpl<>(list, pageable, count);
        
    }

}




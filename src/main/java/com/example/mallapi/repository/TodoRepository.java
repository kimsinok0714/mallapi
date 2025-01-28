package com.example.mallapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.mallapi.domain.Todo;
import com.example.mallapi.repository.search.TodoSearch;


/*
 * JPA의 경우 JpaRepository나 CrudRepository 인터페이스를 상속받은 경우에는 해당 인터페이스에 직접 @Repository 애노테이션을 붙이지 않아도 
 * 스프링 데이터 JPA가 자동으로 빈으로 등록해줍니다.
 */


 public interface TodoRepository extends JpaRepository<Todo, Long>, TodoSearch {



}

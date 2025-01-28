package com.example.mallapi.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.mallapi.domain.Product;
import com.example.mallapi.repository.search.ProductSearch;



public interface ProductRespository extends JpaRepository<Product, Long> , ProductSearch {

    // @EnttiyGraph : PATCH JOIN (Eager Loading)
    // attributePaths를 통해 지정된 연관된 엔티티(imageList)를 즉시 로드(패치)한다.
    @EntityGraph(attributePaths = "imageList")
    @Query("select p from Product p where p.pno = :pno")
    Optional<Product> selectOne(@Param("pno") Long pno);

    
    // UPDATE, DELETE 
    @Modifying
    @Query("update Product p set p.delFlag = :delFlag where p.pno = :pno")
    void updateDelete(@Param("pno") Long pno, @Param("delFlag") boolean delFlag);
    
    // ord = 0 인 경우 대표 이미지 
    @Query("select p, pi from Product p left join p.imageList pi where pi.ord = 0 and p.delFlag = false")
    Page<Object[]> selectList(Pageable pageable);



}



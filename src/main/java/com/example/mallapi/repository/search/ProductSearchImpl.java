package com.example.mallapi.repository.search;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.example.mallapi.domain.Product;
import com.example.mallapi.domain.ProductImage;
import com.example.mallapi.domain.QProduct;
import com.example.mallapi.domain.QProductImage;
import com.example.mallapi.dto.PageRequestDto;
import com.example.mallapi.dto.PageResponseDto;
import com.example.mallapi.dto.ProductDto;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProductSearchImpl extends QuerydslRepositorySupport implements ProductSearch {

    public ProductSearchImpl() {
        super(Product.class);
    }

    @Override
    public PageResponseDto<ProductDto> searchList(PageRequestDto pageRequestDto) {
       
        QProduct product = QProduct.product;
        
        QProductImage productImage = QProductImage.productImage;


        Pageable pageable = PageRequest.of(
                    pageRequestDto.getPage() - 1, 
                    pageRequestDto.getSize(),  
                    Sort.by("pno").descending());
        
       
        JPQLQuery<Product> query = from(product)
                .leftJoin(product.imageList, productImage)
                .where(productImage.ord.eq(0));

        getQuerydsl().applyPagination(pageable, query);

        List<Tuple> productList = query.select(product, productImage).fetch();
        
        List<ProductDto> dtoList = productList.stream().map( tuple -> {

            Product productEntity = tuple.get(product);
            ProductImage productImageEntity = tuple.get(productImage);

            ProductDto productDto = ProductDto.builder()
                .pno(productEntity.getPno())
                .pname(productEntity.getPname())
                .pdesc(productEntity.getPdesc())
                .price(productEntity.getPrice())
                .build();

            String imageStr = productImageEntity.getFileName();
            productDto.setUploadFileNames(List.of(imageStr));
            
            return productDto;            

        }).collect(Collectors.toList());


        long totalCount = query.fetchCount();


        return PageResponseDto.<ProductDto>withAll().dtoList(dtoList).totalCount(totalCount).pageRequestDto(pageRequestDto).build();
        
    }

}

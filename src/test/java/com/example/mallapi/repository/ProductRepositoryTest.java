package com.example.mallapi.repository;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.mallapi.domain.Product;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class ProductRepositoryTest {

    @Autowired
    private ProductRespository productRespository;


    @Test
    public void test() {

        Product product = Product.builder()
            .pname("상품")
            .pdesc("상품 설명")
            .price(1000)
            .build();

        product.addImageString(UUID.randomUUID() + "_" + "image1.jpg");
        product.addImageString(UUID.randomUUID() + "_" + "image2.jpg");
            
        productRespository.save(product);    
        
    }
    
}

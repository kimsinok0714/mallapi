package com.example.mallapi.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.swing.Spring;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.mallapi.domain.Product;
import com.example.mallapi.domain.ProductImage;
import com.example.mallapi.dto.PageRequestDto;
import com.example.mallapi.dto.PageResponseDto;
import com.example.mallapi.dto.ProductDto;
import com.example.mallapi.repository.ProductRespository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;



@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRespository productRespository;

    @Override
    public PageResponseDto<ProductDto> getList(PageRequestDto pageRequestDto) {

        // Spring Data JPA에서 제공하는 페이징 및 정렬 정보를 담는 객체        
        Pageable pageable = PageRequest.of(pageRequestDto.getPage() - 1, pageRequestDto.getSize(), Sort.by("pno").descending());


        // Page 객체 : 페이징 처리된 데이터    
        Page<Object[]> result = productRespository.selectList(pageable);
        
        List<ProductDto> dtoList = result.get().map(arr -> {

            Product product = (Product)arr[0];  
            ProductImage productImage = (ProductImage)arr[1];
            
            ProductDto productDto = ProductDto.builder()
                .pno(product.getPno())
                .pname(product.getPname())
                .pdesc(product.getPdesc())
                .price(product.getPrice())
                .build();
            
            String imageStr = productImage.getFileName();
            productDto.setUploadFileNames(List.of(imageStr));

            return productDto;    

        }).collect(Collectors.toList());


        long totalCount = result.getTotalElements();

        return PageResponseDto.<ProductDto>withAll().dtoList(dtoList).totalCount(totalCount).pageRequestDto(pageRequestDto).build();

    }


    @Override
    public long register(ProductDto productDto) {
        
        Product product = dtoToEntity(productDto);

        log.info("product : {}", product);
        log.info("product.imageList : {}", product.getImageList());


        return productRespository.save(product).getPno();

    }

  

    @Override
    public ProductDto get(Long pno) {

        Optional<Product> result = productRespository.findById(pno);
        
        Product product = result.orElseThrow();
        
        return entityToDto(product);

    }


    private ProductDto entityToDto(Product product) {

        ProductDto productDto = ProductDto.builder()
            .pno(product.getPno())
            .pname(product.getPname())
            .pdesc(product.getPdesc())
            .price(product.getPrice())
            .delFlag(product.isDelFlag())
            .build();

        List<ProductImage> imageList = product.getImageList();

        if (imageList == null || imageList.size() ==0) {
            return productDto;
        }

        List<String> fileNameList = imageList.stream().map(productImage -> productImage.getFileName()).toList();

        productDto.setUploadFileNames(fileNameList);

        return productDto;
    }


    private Product dtoToEntity(ProductDto productDto) {
        Product product = Product.builder()
            .pno(productDto.getPno())
            .pname(productDto.getPname())
            .pdesc(productDto.getPdesc())
            .price(productDto.getPrice())         
            .build();

        List<String> uploadFileNames = productDto.getUploadFileNames();    

        if (uploadFileNames == null || uploadFileNames.size() == 0) {
            return product;
        }

        uploadFileNames.forEach(fileName -> {
            product.addImageString(fileName);
        });

        return product;

    }

    @Override
    public void modify(ProductDto productDto) {
       
        // 조회
        Optional<Product> result = productRespository.findById(productDto.getPno());

        // 변경 내용 변경
        Product product = result.orElseThrow();
        product.changeName(productDto.getPname());
        product.changePrice(productDto.getPrice());
        product.changeDesc(product.getPdesc());
        product.changeDel(productDto.isDelFlag());
        
        // 이미지 처리
        product.clearList();

        List<String> uploadFileNames = productDto.getUploadFileNames();
        
        if (uploadFileNames != null &&  !uploadFileNames.isEmpty()) {
            uploadFileNames.forEach(uploadFileName -> {
                product.addImageString(uploadFileName);
            });
        }
        
        productRespository.save(product);
        
    }


    @Override
    public void remove(Long pno) {
        
        productRespository.deleteById(pno);
        
    }
    

}

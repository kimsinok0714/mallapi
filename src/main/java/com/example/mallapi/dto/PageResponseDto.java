package com.example.mallapi.dto;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.Builder;
import lombok.Data;

/*
 PageResponseDto(
    dtoList=[ProductDto(pno=1, pname=냉장고, price=1, pdesc=냉장고입니다., delFlag=false, files=[], 
             uploadFileNames=[c018df2a-0c01-45e1-886c-00321d21ee00_깃.png])], 
	 pageNumList=[1], 
	 pageRequestDto=PageRequestDto(page=1, size=10), 
	 prev=false, 
	 next=false, 
	 totalCount=1, 
	 prevPage=0, 
	 nextPage=0, 
	 totalPage=1, 
	 current=1)

 */





@Data
public class PageResponseDto<E> {

    private List<E> dtoList;

    private PageRequestDto pageRequestDto;

    private int totalCount;
    
    private List<Integer> pageNumList;

    private boolean prev, next;

    private int prevPage, nextPage, totalPage, current;

    
    @Builder(builderMethodName = "withAll")
    public PageResponseDto(List<E> dtoList, PageRequestDto pageRequestDto, long totalCount) {

        this.dtoList = dtoList;
        this.pageRequestDto = pageRequestDto;
        this.totalCount = (int)totalCount; 



        // 한 번에 표시할 페이지 번호의 개수.
        int pageBlock = 10;  //  [prev] 11 12 13 14 15 16 17 18 19 20 [next]  ,  1 2 3 4 5 6 7 8 9 10 [next], 1  2 

        // 현재 페이징 블록의 마지막 페이지 번호.
        int end = (int)(Math.ceil(pageRequestDto.getPage() / (double)pageBlock)) * pageBlock;

        // 현재 페이징 블록의 첫 번째 페이지 번호.
        int start = end - (pageBlock - 1);

        // 전체 데이터의 마지막 페이지 번호.
        int last = (int)(Math.ceil(totalCount / (double)pageRequestDto.getSize())); 

        end = end > last ? last : end;

        //이전 블록이 있는지 여부를
        
        this.prev = start > 1;  

        // 다음 블록이 있는지 여부
        this.next = totalCount > (end * pageRequestDto.getSize());  

        this.pageNumList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());

        this.prevPage = prev ? start - 1 : 0;

        this.nextPage = next ? end + 1 : 0; 

        this.totalPage = this.pageNumList.size();
        
        this.current = pageRequestDto.getPage();
        

    }


}

package com.example.mallapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberModifyDto {
    
    private String email;
    private String pw;
    private String nickname;



}

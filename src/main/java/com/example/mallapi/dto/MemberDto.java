package com.example.mallapi.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;


// User implements UserDetails
public class MemberDto extends User {

    private String email;
    private String nickname;
    private String pw;
    private boolean social;
    private List<String> roleNames = new ArrayList<>();

    
    // username == email
    public MemberDto(String email, String pw, String nickname, boolean social, List<String> roleNames) {
      
        super(email, pw, roleNames.stream().map(roleName -> new SimpleGrantedAuthority("ROLE_" + roleName)).collect(Collectors.toList()));
        
        this.email = email;
        this.pw = pw;
        this.nickname = nickname;        
        this.social = social;
        this.roleNames = roleNames;
    }

    
    // JWT 문자열 : Header, Claim, Signature로 구성됨
    // Claim (Payload) 정보
    public Map<String, Object> getClaims() {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("email", email);
        dataMap.put("pw", pw);
        dataMap.put("nickname", nickname);
        dataMap.put("social", social);
        dataMap.put("roleNames", roleNames);        
        return dataMap;
    }
}
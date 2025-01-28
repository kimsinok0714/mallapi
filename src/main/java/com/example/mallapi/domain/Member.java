package com.example.mallapi.domain;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "") // 연관관계가 있는 경우 반드시 사용
public class Member {

    @Id
    private String email;  
    private String pw;
    private String nickname;
    private boolean social;  // 소셜 로그인 여부

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default 
    private List<MemberRole> memberRoleList = new ArrayList<>();

    // 권한 추가
    public void addRole(MemberRole memberRole) {
        memberRoleList.add(memberRole);
    }

	// 권한 삭제
    public void clearRole() {
        memberRoleList.clear();
    }

    public void changePw(String pw) {
        this.pw = pw;
    } 

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public void changeSocial(boolean social) {
        this.social = social;
    }

}
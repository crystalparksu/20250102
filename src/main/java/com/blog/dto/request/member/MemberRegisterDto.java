package com.blog.dto.request.member;


import com.blog.common.Role;
import com.blog.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
* -Register -
* 회원가입 요청 dto
*/
@Getter
@Setter
@NoArgsConstructor
public class MemberRegisterDto {
    //이메일,패스워드,패스워드체크,사용자이름
    private  String email;
    private  String password;
    private  String passwordCheck;
    private  String username;


    @Builder
    public MemberRegisterDto(String email, String password, String passwordCheck, String username) {
        this.email = email;
        this.password = password;
        this.passwordCheck = passwordCheck;
        this.username = username;
    }

    //DTO -> Entity
    public static Member ofEntity(MemberRegisterDto dto){
        return Member.builder()
                .email(dto.getEmail())
                .password(dto.getPassword())
                .username(dto.getUsername())
                .roles(Role.USER)
                .build();
    }
}









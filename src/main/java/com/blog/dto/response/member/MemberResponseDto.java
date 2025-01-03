package com.blog.dto.response.member;


import com.blog.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
 * -Response -
 * 사용자 정보 반환 dto
 */
@Getter
@Setter
@NoArgsConstructor
public class MemberResponseDto {

    private  String email;
    private  String username;

    @Builder
    public MemberResponseDto(String email, String username) {
        this.email = email;
        this.username = username;
    }

    //Entity -> DTO
    public  static  MemberResponseDto fromEntity(Member member){
        return MemberResponseDto.builder()
                .email(member.getEmail())
                .username(member.getusername())
                .build();
    }
}

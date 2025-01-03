package com.blog.controller;


import com.blog.dto.request.member.MemberLoginDto;
import com.blog.dto.request.member.MemberRegisterDto;
import com.blog.dto.request.member.MemberUpdateDto;
import com.blog.dto.response.member.MemberResponseDto;
import com.blog.dto.response.member.MemberTokenDto;
import com.blog.entity.Member;
import com.blog.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class MemberController {

    //멤버 서비스(메서드)
    private final MemberService memberService;



    //-------- 아이디 체크 조회
    @GetMapping("/checkId")
    public ResponseEntity<?> checkIdDuplicate(
            @RequestParam String email){
        memberService.checkIdDuplicate(email);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    //회원가입 요청  -RequestBody
    @PostMapping("/register")
    public ResponseEntity<MemberResponseDto> register(
            @RequestBody MemberRegisterDto memberRegisterDto){

        MemberResponseDto successMember = memberService.register(memberRegisterDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(successMember);
    }


    //로그인 요청
    @PostMapping("/login")
    public ResponseEntity<MemberTokenDto> login(
            @RequestBody MemberLoginDto memberLoginDto){

        MemberTokenDto loginDTO = memberService.login(memberLoginDto);
        return ResponseEntity.status(HttpStatus.OK).header(loginDTO.getToken()).body(loginDTO);
    }


    //비밀번호 체크 요청
    @PostMapping("/checkPwd")
    public ResponseEntity<MemberResponseDto> check(
            @AuthenticationPrincipal Member member,
            @RequestBody Map<String,String> request){

        String password = request.get("password");
        MemberResponseDto memberInfo = memberService.check(member,password);
        return ResponseEntity.status(HttpStatus.OK).body(memberInfo);
    }


    //회원정보 수정
    @PutMapping("/update")
    public ResponseEntity<MemberResponseDto> update(
            @AuthenticationPrincipal Member member,
            @RequestBody MemberUpdateDto memberUpdateDto
            ){
        MemberResponseDto memberUpdate = memberService.update(member,memberUpdateDto);
        return ResponseEntity.status(HttpStatus.OK).body(memberUpdate);
    }
}

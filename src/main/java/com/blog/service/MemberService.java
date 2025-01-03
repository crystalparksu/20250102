package com.blog.service;

//!!

import com.blog.common.exception.MemberException;
import com.blog.common.exception.ResourceNotFoundException;
import com.blog.dto.request.member.MemberLoginDto;
import com.blog.dto.request.member.MemberRegisterDto;
import com.blog.dto.request.member.MemberUpdateDto;
import com.blog.dto.response.member.MemberResponseDto;
import com.blog.dto.response.member.MemberTokenDto;
import com.blog.entity.Member;
import com.blog.repository.MemberRepository;
import com.blog.security.jwt.CustomUserDetailsService;
import com.blog.security.jwt.JwtTokenUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MemberService {

    //리파지토리
    private final MemberRepository memberRepository;

    //인증
    private final CustomUserDetailsService customUserDetailsService;
    private final AuthenticationManager authenticationManager;

    //패스워드 인코딩
    private final PasswordEncoder encoder;

    //토큰
    private  final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService userDetailsService;


    //메서드(1): 아이디 중복 체크
    public HttpStatus checkIdDuplicate(String email){
        //사용자 아이디 여부 확인
        isExistUserEmail(email);
        return HttpStatus.OK;
    }

    //메서드(2): 회원가입
    public MemberResponseDto register(MemberRegisterDto registerDto){
        //1.아이디 중복 이메일 체크(사용자 아이디 확인)
        isExistUserEmail(registerDto.getEmail());

        //2.패스워드 체크
        checkPassword(registerDto.getPassword(),registerDto.getPasswordCheck());

        //3.패스워드 암호화
        String encodePwd = encoder.encode(registerDto.getPassword());
        registerDto.setPassword(encodePwd);

        //4-1.회원 Member 리파지토리에 저장
        //4-2.DTO를 Entity로 변환
        Member saveMember = memberRepository.save(MemberRegisterDto.ofEntity(registerDto));

        //5.Entity -> DTO 변환
        return MemberResponseDto.fromEntity(saveMember);
    }


    //메서드(3): 로그인
    public MemberTokenDto login(MemberLoginDto loginDto){
        //1.인증매니저
        authenticate(loginDto.getEmail(),loginDto.getPassword());

        //2.사용자 정보 담고 있는
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginDto.getEmail());
        //3.패스워드 인코드
        checkEncodePassword(loginDto.getPassword(), userDetails.getPassword());

        //4.토큰 생성
        String token = jwtTokenUtil.generateToken(userDetails);
        //5.반환
        return  MemberTokenDto.fromEntity(userDetails, token);
    }





    //메서드(4): 체크
    public MemberResponseDto check(Member member, String password){
        //1.
        Member checkMember = (Member) userDetailsService.loadUserByUsername(member.getEmail());

        //2.
        checkEncodePassword(password,checkMember.getPassword());

        //3.
        return MemberResponseDto.fromEntity(checkMember);
    }


    //메서드(5): 수정
    public MemberResponseDto update(Member member, MemberUpdateDto updateDto){
        //1.비밀번호 체크
        checkPassword(updateDto.getPassword(), updateDto.getPasswordCheck());

        //2.비밀번호 인코드
        String encodePwd = encoder.encode(updateDto.getPassword());

        //3.멤버 리파지토리에
        Member updateMember = memberRepository.findByEmail(member.getEmail()).orElseThrow(
                ()-> new ResourceNotFoundException("Member", "Member Email", member.getEmail())
        );

        //4.회원정보 수정
        updateMember.update(encodePwd, updateDto.getUsername());

        //5. 반환
        return MemberResponseDto.fromEntity(updateMember);
    }






    //--------------■사용자 인증(파라미터 email/password)
    //authenticationManager -> authenticate()
    //UsernamePasswordAuthenticationToken 구현객체 활용
    private void authenticate(String email, String pwd){
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email,pwd));
        }catch (DisabledException e){ //로그인 실패 시, 대응 예외
            throw new MemberException("인증되지 않은 아이디입니다.", HttpStatus.BAD_REQUEST);
        }catch (BadCredentialsException e){ //사용자 인증 시, 적절하지 않을 때 예외
            throw new MemberException("비밀번호가 일치하지 않습니다..", HttpStatus.BAD_REQUEST);
        }
    }



    //아이디(이메일)중복 체크
    // @param email
    private  void  isExistUserEmail(String email){
        if(memberRepository.findByEmail(email).isPresent()){
            throw new MemberException("이미 사용 중인 이메일입니다.", HttpStatus.BAD_REQUEST);
        }
    }



    //비밀번호,비밀번호 확인 같은지 체크
    //@param password
     //@param passwordCheck
    private void  checkPassword(String password, String passwordCheck){
        if(!password.equals(passwordCheck)){
            throw new MemberException("패스워드 불일치",HttpStatus.BAD_REQUEST);
        }
    }



    //사용자 입력한 비밀번호, DB에 저장된 비밀번호 같은지 체크: 인코딩 확인
    //rawPassword
    //encodedPassword
    //Java - String.matches()로 문자열패턴, 특정 패턴 문자열 포함 여부
    private void checkEncodePassword(String rawPassword,
                                     String encodedPassword){
        if(!encoder.matches(rawPassword,encodedPassword)){
            throw new MemberException("패스워드  불일치",HttpStatus.BAD_REQUEST);
        }
    }



}












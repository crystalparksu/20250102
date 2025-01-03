package com.blog.security.jwt;



/* Jwt 토큰의 유효성 검사/인증 */

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    //필드
    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    //jwt
    @Value("${jwt.header}") private String HEADER_STRING;
    @Value("${jwt.prefix}") private String TOKEN_PREFIX;

    //사용자 직접 구현
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        //스레드
        Thread currentThread = Thread.currentThread();
        log.info("현재 실행 중인 스레드: " + currentThread.getName());

        //get token
        String header = request.getHeader(HEADER_STRING);
        String username = null;
        String authToken = null;


        //prefix
        if(header != null && header.startsWith(TOKEN_PREFIX)){
            authToken = header.replace(TOKEN_PREFIX, " ");
            try{
                username = this.jwtTokenUtil.getUsernameFromToken(authToken);
            }catch (IllegalArgumentException ex){ //IllegalArgumentException - 잘못된 인수
                //사용자 ID를 얻는데 실패함.
                log.info("사용자 ID를 얻는데 실패함.");
                ex.printStackTrace();
            }catch (ExpiredJwtException ex){ // ExpiredJwtException - 토큰 만료
                //토큰 만료
                log.info("토큰 만료");
                ex.printStackTrace();
            }catch (MalformedJwtException ex){ //MalformedJwtException -잘못된 토큰
                //잘못된 JWT(토큰)
                log.info("잘못된 JWT(토큰)");
                ex.printStackTrace();
            }catch (Exception e){ //Exception -예외
                //JWT 토큰을 얻을 수 없음.
                log.info("JWT 토큰을 얻을 수 없습니다.");
                e.printStackTrace();
            }
        }else {
            log.info("JWT는 Bearer로 시작하지 않습니다.");
        }


        if ((username != null) && (SecurityContextHolder.getContext().getAuthentication() == null)) {
            //log.info(SecurityContextHolder.getContext().getAuthentication().getName());
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            if (this.jwtTokenUtil.validateToken(authToken, userDetails)) {


                UsernamePasswordAuthenticationToken authenticationToken =

                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                log.info("authenticated user " + username + ", setting security context");
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            } else {
                log.info("잘못된 JWT 토큰입니다");
            }
        } else {
            log.info("사용자 이름이 null 이거나, context(컨텍스트)가 null이 아닙니다.");
        }
        filterChain.doFilter(request, response);
    }
}

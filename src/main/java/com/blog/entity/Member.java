package com.blog.entity;

import jakarta.persistence.*;
import com.blog.common.BaseTimeEntity;
import com.blog.common.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Getter
@Entity
@NoArgsConstructor
public class Member extends BaseTimeEntity implements UserDetails {

    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;


    // 이메일로 로그인함
    @Column(nullable = false)
    private String email;


    @Column(nullable = false)
    private String password;


    @Column(nullable = false)
    private String username;


    @Enumerated(EnumType.STRING)
    private Role roles;


    // ===== JPA 관계 매핑 =====
    //＃일대다: 한 회원(Member)의 여러 게시글(Board)
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public List<Board> boards = new ArrayList<>();

    //＃일대다: 한 회원(Member)의 여러 댓글(Comment)
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public List<Comment> comments = new ArrayList<>();


    //== 생성자 Builder ==//
    @Builder
    public Member(String email, String password, String username, Role roles) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.roles = roles;
    }


    //== 회원정보 수정 (update) ==//
    public void update(String password, String username) {
        this.password = password;
        this.username = username;
    }

    //========== UserDetails implements ==========//
    /**
     * Token을 고유한 Email 값으로 생성합니다
     * @return email;
     */
    @Override
    public String getUsername() { //사용자 이름 가져오기
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {//권한 얻기
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add( new SimpleGrantedAuthority("ROLE_" + this.roles.name()));
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {//계정이 만료되지 않음
        return true;
    }

    @Override
    public boolean isAccountNonLocked() { //계정이 잠금되지 않음
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {//자격 증명이 만료되지 않음
        return true;
    }

    @Override
    public boolean isEnabled() {//활성화됨
        return true;
    }
}

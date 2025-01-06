package com.blog.entity;

import jakarta.persistence.*;
import com.blog.common.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "COMMENT_ID")
    private Long id;

    private String content;


    // ===== JPA 관계 매핑 =====
    //＃다대일: 여러 개의 댓글(Comment)이 한 회원(Member)에 속해있는 것
    @ManyToOne(fetch = FetchType.LAZY) //지연로딩
    @JoinColumn(name = "MEMBER_ID") //조인할 컬럼(MEMBER_ID)
    public Member member;


    //＃다대일: 여러 개의 댓글(Comment)이 한 게시글(Board)에 속해있는 것
    @ManyToOne(fetch = FetchType.LAZY) //지연로딩
    @JoinColumn(name = "BOARD_ID")
    public Board board;



    @Builder
    public Comment(Long id, String content, Member member, Board board) {
        this.id = id;
        this.content = content;
        this.member = member;
        this.board = board;
    }

    // #게시판 (Board)와의 다대일(N:1) 관계를 설정하는 메소드
    public void setBoard(Board board) {
        this.board = board;
        board.getComments().add(this); // Board 엔티티에도 Comment를 추가합니다.
    }


    // #회원(Member)와의 다대일(N:1) 관계를 설정하는 메소드
    public void setMember(Member member) {
        this.member = member;
        member.getComments().add(this); // Member 엔티티에도 Comment를 추가합니다.
    }


    // #댓글 수정(update) 메서드
    public void update(String content) {
        this.content = content;
    }
}

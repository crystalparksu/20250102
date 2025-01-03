package com.blog.entity;
import com.blog.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name="COMMENT_ID")
    private Long id;

    private String content;

    //회원
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    public Member member;

    //게시판
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    public Board board;


    @Builder
    public Comment(Long id, String content, Member member, Board board) {
        this.id = id;
        this.content = content;
        this.member = member;
        this.board = board;
    }


    // Board 와의 다대일(n:1)관계를 설정하는 메서드
    public void  setBoard(Board board){
        this.board = board;
        board.getComments().add(this);
    }


    //Member와의 다대일(n:1)관계를 설정하는 메서드
    public void  setMember(Member member){
        this.member = member;
        member.getComments().add(this);
    }

    //메서드
    //댓글 수정 메서드
    public void update(String content){
        this.content = content;
    }

}

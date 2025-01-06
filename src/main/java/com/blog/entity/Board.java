package com.blog.entity;

import jakarta.persistence.*;
import com.blog.common.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.engine.jdbc.batch.spi.Batch;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Board extends BaseTimeEntity {
    @Id @GeneratedValue
    @Column(name = "BOARD_ID")
    private Long id;

    @Column(nullable = false)
    private String title;

    private String content;

    @Column(name = "VIEW_COUNT")
    private int viewCount;

    // ===== JPA 관계 매핑 =====
    //＃다대일: 여러 개의 게시판(Board)이 한 개(One)의 MEMBER_ID에 속해있는 것
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID") //조인해야하는 칼럼(MEMBER_ID)
    public Member member;


    //＃일대다: 하나의 게시판(Board)에 여러 개의 댓글
    /*1,2,3,4,5,6,7,8,9,10은 기본적으로 모두 캐싱하기 때문에
    Batch Size가 10보다 클 경우 1~10은 항상 캐싱 케이스에 포함된다고 합니다.*/
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @BatchSize(size = 10) //*캐싱 케이스 최적화하기
    public List<Comment> comments = new ArrayList<>();


    //＃일대다: 하나의 게시판(Board)에 여러 개의 파일
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @BatchSize(size = 10) //*캐싱 케이스 최적화하기
    public List<FileEntity> files = new ArrayList<>();



    @Builder
    public Board(Long id, String title, String content, int viewCount, Member member) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.member = member;
    }

    //== 조회수 증가 ==//
    public void upViewCount() {
        this.viewCount++;
    }

    //== 수정 Dirty Checking ==//
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    //== Member & Board 연관관계 편의 메소드 ==//
    public void setMappingMember(Member member) {
        this.member = member;
        member.getBoards().add(this);
    }
}

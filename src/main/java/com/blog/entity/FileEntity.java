package com.blog.entity;

import jakarta.persistence.*;
import com.blog.common.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "FILE")
@Getter
@NoArgsConstructor
public class FileEntity extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "FILE_ID")
    private Long id;

    @Column(name = "ORIGIN_FILE_NAME")
    private String originFileName;

    @Column(name = "FILE_TYPE")
    private String fileType;

    @Column(name = "FILE_PATH")
    private String filePath;



    // ===== JPA 관계 매핑 =====
    //＃다대일: 여러 개의 파일(FileEntity)이 하나의 게시글(Board)에 속해있는 것
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_ID") //조인할 컬럼(BOARD_ID)
    public Board board;


    @Builder
    public FileEntity(Long id, String originFileName, String filePath, String fileType) {
        this.id = id;
        this.originFileName = originFileName;
        this.filePath = filePath;
        this.fileType = fileType;
    }


    // #게시글(Board) 와의 다대일(N:1) 관계를 설정하는 메소드
    public void setMappingBoard(Board board) {
        this.board = board;
    }
}

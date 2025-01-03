package com.blog.entity;


import com.blog.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "FILE")
@Getter
@NoArgsConstructor
public class FileEntity extends BaseTimeEntity {


    @Id
    @GeneratedValue
    @Column(name="FILE_ID")
    private Long id;

    @Column(name="ORIGIN_FILE_NAME")
    private String originFileName;

    @Column(name="FILE_TYPE")
    private String fileType;

    @Column(name="FILE_PATH")
    private String filePath;


    //게시판
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_ID")
    public  Board board;


    @Builder
    public FileEntity(Long id, String originFileName, String fileType, String filePath, Board board) {
        this.id = id;
        this.originFileName = originFileName;
        this.fileType = fileType;
        this.filePath = filePath;
        this.board = board;
    }

    //다대일(N:1) 관계를 설정하는 메소드
    public void setMappingBoard(Board board){
        this.board=board;
    }
}

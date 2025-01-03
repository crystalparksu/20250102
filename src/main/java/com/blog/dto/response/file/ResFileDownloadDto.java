package com.blog.dto.response.file;


import com.blog.entity.FileEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * -Response-
 *  파일 다운로드 후 응답 DTO
 */

@Getter
@Setter
@NoArgsConstructor
public class ResFileDownloadDto {

    private String fileName;
    private String fileType;
    private byte[] content;


    @Builder
    public ResFileDownloadDto(String fileName, String fileType, byte[] content) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.content = content;
    }
    //File - fromFileResource
    public  static  ResFileDownloadDto fromFileResource(
            FileEntity file,
            String contentType,
            byte[] content){

        return ResFileDownloadDto.builder()
                .fileName(file.getOriginFileName())
                .fileType(contentType)
                .content(content)
                .build();
    }

}













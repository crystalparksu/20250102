package com.blog.controller;

import com.blog.dto.request.comment.CommentDto;
import com.blog.dto.response.comment.ResCommentDto;
import com.blog.entity.Member;
import com.blog.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/board/{boardId}/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    //댓글 목록(페이징)
    @GetMapping("/list")
    public ResponseEntity<Page<ResCommentDto>> commentList(
            @PathVariable Long boardId,
            @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ResCommentDto> commentList = commentService.getAllComments(pageable,boardId);
        return ResponseEntity.status(HttpStatus.OK).body(commentList);
    }

    //댓글 등록
    @PostMapping("/write")
    public ResponseEntity<ResCommentDto> write(
            @AuthenticationPrincipal Member member,
            @PathVariable Long boardId,
            @RequestBody CommentDto commentDto){
        ResCommentDto saveCommentDto = commentService.write(boardId,member,commentDto);
        return  ResponseEntity.status(HttpStatus.OK).body(saveCommentDto);
    }

    //댓글 수정
    @PatchMapping("/update/{commentId}")
    public ResponseEntity<ResCommentDto> update(
            @PathVariable Long commentId,
            @RequestBody CommentDto commentDto){

        ResCommentDto updateCommentDto = commentService.update(commentId,commentDto);
        return ResponseEntity.status(HttpStatus.OK).body(updateCommentDto);
    }


    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<Long> delete(@PathVariable Long commentId){
        commentService.delete(commentId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}










package com.blog.controller;


import com.blog.dto.request.board.BoardUpdateDto;
import com.blog.dto.request.board.BoardWriteDto;
import com.blog.dto.request.board.SearchData;
import com.blog.dto.response.board.ResBoardDetailsDto;
import com.blog.dto.response.board.ResBoardListDto;
import com.blog.dto.response.board.ResBoardWriteDto;
import com.blog.entity.Member;
import com.blog.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    //게시글
    private final BoardService boardService;

    //게시물 전체 조회 - (페이징 목록) - list
    @GetMapping("/list")
    public ResponseEntity<Page<ResBoardListDto>> boardList(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ResBoardListDto> listDTO = boardService.getAllBoards(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(listDTO);
    }


    //게시물 검색 -(페이징 검색) - search
    @GetMapping("/search")
    public ResponseEntity<Page<ResBoardListDto>> search(
            @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam String writerName){

        SearchData searchData = SearchData.createdSearchData(title,content,writerName);
        Page<ResBoardListDto> searchBoard = boardService.search(searchData,pageable);
        return ResponseEntity.status(HttpStatus.OK).body(searchBoard);
    }

    //게시물 등록 - write
    @PostMapping("/write")
    public ResponseEntity<ResBoardWriteDto> write(
            @RequestBody BoardWriteDto boardDTO,
            @AuthenticationPrincipal Member member){
    //1.스레드 등록
    Thread currentThread = Thread.currentThread();
    log.info("현재 실행 중인 스레드: " + currentThread.getName());

        ResBoardWriteDto saveBoardDTO = boardService.write(boardDTO,member);
        return ResponseEntity.status(HttpStatus.CREATED).body(saveBoardDTO);

    }



    //게시물 상세 - detail
    @GetMapping("/{boardId}")
    public ResponseEntity<ResBoardDetailsDto> detail(
            @PathVariable("boardId") Long boardId){
        ResBoardDetailsDto findBoardDTO = boardService.detail(boardId);
        return ResponseEntity.status(HttpStatus.OK).body(findBoardDTO);
    }


    //게시물 수정(상세보기 -> 수정) - update
    @PatchMapping("/{boardId}/update")
    public ResponseEntity<ResBoardDetailsDto> update(
            @PathVariable Long boardId,
            @RequestBody BoardUpdateDto boardDTO){
        ResBoardDetailsDto updateBoardDTO = boardService.update(boardId,boardDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updateBoardDTO);
    }


    //게시물 삭제(상세보기 -> 삭제) - delete
    @DeleteMapping("/{boardId}/delete")
    public ResponseEntity<Long> delete(@PathVariable Long boardId){
        boardService.delete(boardId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }




}

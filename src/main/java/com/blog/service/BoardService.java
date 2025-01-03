package com.blog.service;


import com.blog.dto.request.board.SearchData;
import com.blog.dto.response.board.ResBoardListDto;
import com.blog.entity.Board;
import com.blog.repository.BoardRepository;
import com.blog.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {
    private  final BoardRepository boardRepository;
    private  final MemberRepository memberRepository;

    //페이징 리스트
    public Page<ResBoardListDto> getAllBoard(Pageable pageable){

        Page<Board> boards = boardRepository
                .findAllWithMemberAndComments(pageable);

        List<ResBoardListDto> list = boards.getContent()
                .stream().map(ResBoardListDto::fromEntity)
                .collect(Collectors.toList());

        return new PageImpl<>(list,pageable,boards.getTotalElements());
    }

    //게시글 검색 isEmpty()
    public  Page<ResBoardListDto> search(
            SearchData searchData, Pageable pageable){

        //1.
        Page<Board> result = null;
        if(!searchData.getTitle().isEmpty()){
            result = boardRepository.findAllTitleContaining(searchData.getTitle(),pageable);
        } else if (!searchData.getContent().isEmpty()) {
            result = boardRepository.findAllContentContaining(searchData.getContent(),pageable);
        }else if (!searchData.getWriterName().isEmpty()) {
            result = boardRepository.findAllUsernameContaining(searchData.getWriterName(),pageable);
    }
        List<ResBoardListDto> list  = result.getContent()
                .stream().map(ResBoardListDto::fromEntity)
                .collect(Collectors.toList());

        return new PageImpl<>(list, pageable,result.getTotalElements());



    //게시글 등록

    //게시글 상세보기

    //게시글 수정

    //게시글 삭제
}

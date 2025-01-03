package com.blog.repository;

import com.blog.entity.Board;
import com.blog.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    //게시글 상세 조회
    @Query()
    Optional<Board> findByIdWithMemberAndCommentsAndFiles(Long boardId);

    //첫 페이징 화면
    @Query()
    Page<Board> findAllWithMemberAndComments(Pageable pageable);

    //제목 검색
    @Query()
    Page<Board> findAllTitleContaining(String title, Pageable pageable);

    //내용 검색
    @Query()
    Page<Board> findAllContentContaining(String content, Pageable pageable);

    //작성자 검색
    @Query()
    Page<Board> findAllUsernameContaining(String username, Pageable pageable);
}

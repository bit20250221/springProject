package org.spring.attraction.repository;

import org.spring.attraction.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface Board_repository extends JpaRepository<Board,Long> {

    //페이징 처리가 제외된 특정 탭 그리고 작성자 아이디, 제목, 내용에 따른 검색 쿼리
    @Query(value = "select b from Board b Where b.tab = :tab and b.title like %:keyword%")
    List<Board> boardTitleSearch(@Param("tab") String tab, @Param("keyword") String keyword);

    @Query(value = "select b from Board b Where b.tab = :tab and b.content like %:keyword%")
    List<Board> boardContentSearch(@Param("tab") String tab, @Param("keyword") String keyword);

    @Query(value = "select b from Board b Where b.tab = :tab and b.user.user_login_Id like %:keyword%")
    List<Board> boardWriterSearch(@Param("tab") String tab, @Param("keyword") String keyword);

    //페이징 처리가 포함된 전체 검색
    @Query(value = "select b from Board b where b.title like %:keyword% or " +
            "b.content like %:keyword% or b.user.user_login_Id like %:keyword%")
    Page<Board> findByAllKeyword(@Param("keyword") String keyword, Pageable pageable);

    //페이징 처리가 포함된 탭을 이용한 검색
    @Query("select b from Board b where b.tab = :tab")
    Page<Board> findByTab(@Param("tab") String tab, Pageable pageable);

    //페이징 처리가 포함된 탭과 키워드를 이용한 검색
    @Query("select b from Board b where (b.tab = :tab) and (b.title like %:keyword% " +
            "or b.content like %:keyword% or b.user.user_login_Id like %:keyword%)")
    Page<Board> findByTabSearch(@Param("tab") String tab,@Param("keyword") String keyword, Pageable pageable);

    //페이징 처리가 포함된 특정 탭 그리고 작성자 아이디, 제목, 내용에 따른 검색 쿼리
    @Query(value = "select b from Board b Where b.tab = :tab and b.title like %:keyword%")
    Page<Board> boardTitleSearch(@Param("tab") String tab, @Param("keyword") String keyword, Pageable pageable);

    @Query(value = "select b from Board b Where b.tab = :tab and b.content like %:keyword%")
    Page<Board> boardContentSearch(@Param("tab") String tab, @Param("keyword") String keyword, Pageable pageable);

    @Query(value = "select b from Board b Where b.tab = :tab and b.user.user_login_Id like %:keyword%")
    Page<Board> boardWriterSearch(@Param("tab") String tab, @Param("keyword") String keyword, Pageable pageable);


    //페이징 처리가 포함된 작성자 아이디, 제목, 내용에 따른 검색 쿼리
    Page<Board> findByTitleContaining(String keyword, Pageable pageable);

    Page<Board> findByContentContaining(String keyword, Pageable pageable);

    @Query(value = "select b from Board b Where b.user.user_login_Id like %:keyword%")
    Page<Board> findByWriterIdContaining(@Param("keyword") String keyword, Pageable pageable);

}

package com.softj.pwg.repo;

import com.softj.pwg.entity.Board;
import com.softj.pwg.entity.Coment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface ComentRepo extends JpaRepository<Coment, Long>, QuerydslPredicateExecutor<Coment> {
    //board 테이블을 다 가져오는데 조건걸어서 나라별로 session에 저장된 나라값만 갖고옴
    //List<Coment> findAllByBoardAndIsDelFalse (Board board);
    List<Coment> findAllByBoardAndIsDelFalseOrderBySeqDesc (Board board);
    //List<Coment> findAllByBoardAndIsDelFalseOrderBycreatedIdDesc(Board board); 정렬실패
    //List<Coment>findAll(Sort.Direction.DESC,"createdAt"))ByBoardAndIsDelFalse(Board board); 정렬실패
    Coment findBySeq(long seq);



}


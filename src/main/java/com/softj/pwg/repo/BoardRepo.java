package com.softj.pwg.repo;

import com.querydsl.core.BooleanBuilder;
import com.softj.pwg.entity.Board;
import com.softj.pwg.entity.Coment;
import com.softj.pwg.entity.User;
import com.softj.pwg.vo.ParamVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface BoardRepo extends JpaRepository<Board, Long>, QuerydslPredicateExecutor<Board> {
    //board 테이블을 다 가져오는데 조건걸어서 나라별로 session에 저장된 나라값만 갖고옴

    Board findBySeq(long seq);
   // Board findSeqById(String id);//아이디로 보드 시퀀스를 가져옴.
    List<Board> findAllByNation(String nation);
    //List<Board>findAllByOrderBySeqDesc(Board);





}

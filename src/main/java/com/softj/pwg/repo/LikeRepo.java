package com.softj.pwg.repo;

import com.softj.pwg.entity.Board;
import com.softj.pwg.entity.Like;
import com.softj.pwg.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface LikeRepo extends JpaRepository<Like, Long>, QuerydslPredicateExecutor<Like> {
    long countByBoard(Board board);
    Like findByBoardAndUser(Board board,User user);
}

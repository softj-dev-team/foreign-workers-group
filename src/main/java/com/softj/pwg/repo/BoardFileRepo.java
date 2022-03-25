package com.softj.pwg.repo;

import com.softj.pwg.entity.Board;
import com.softj.pwg.entity.BoardFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface BoardFileRepo extends JpaRepository<BoardFile, Long>, QuerydslPredicateExecutor<BoardFile> {
}

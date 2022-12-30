package com.softj.pwg.repo;

import com.softj.pwg.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User, Long>, QuerydslPredicateExecutor<User> {
    //user entity 넣어주고 pk 넣어줌. pk 타임 / QuerydslPredicateExecutorquerydsl 로 jpql을 동적쿼리 사용하게 쉽게 사용하기 위해서.
    @Query("select count(a) from User a where a.nickname = :nickname and a.seq != :seq")
    long countByNickname(String nickname, long seq);
    User findFirstByIdAndPlatform(String id,String platform);
    User findFirstById(String id);
    //User findBySeq(long seq);
   // User findSeqById(String id);// 아이디로 시퀀스 가져옴.

    //update 테이블명 set 닉네임 = 어떤닉네임인지 where 플랫폼 = 뭔플랫폼 and id = 뭔id;

}
package com.softj.pwg.repo;

import com.softj.pwg.entity.Nation;
import com.softj.pwg.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NationRepo extends JpaRepository<Nation, Long>, QuerydslPredicateExecutor<Nation> {
    List<Nation> findAllByIsPublicTrueOrderBySortAsc();
    List<Nation> findAllByOrderBySortAsc();
    Nation findBySort(int sort);
    @Query("select coalesce(max(a.sort)+1, 1) from Nation a")
    int maxSort();
}
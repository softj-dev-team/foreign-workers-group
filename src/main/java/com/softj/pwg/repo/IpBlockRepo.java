package com.softj.pwg.repo;

import com.softj.pwg.entity.IpBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface IpBlockRepo extends JpaRepository<IpBlock, Long>, QuerydslPredicateExecutor<IpBlock> {
}

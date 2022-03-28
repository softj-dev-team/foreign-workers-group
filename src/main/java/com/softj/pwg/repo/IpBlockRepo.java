package com.softj.pwg.repo;

import com.softj.pwg.entity.IpBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

public interface IpBlockRepo extends JpaRepository<IpBlock, Long>, QuerydslPredicateExecutor<IpBlock> {
    IpBlock findFirstByIp(@Param("ip")String ip);
}

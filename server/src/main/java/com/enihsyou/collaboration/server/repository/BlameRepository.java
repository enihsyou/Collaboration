package com.enihsyou.collaboration.server.repository;

import com.enihsyou.collaboration.server.domain.CoBlame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * 对{@link CoBlame}数据库表操作的DAO
 */
@Repository
public interface BlameRepository extends JpaRepository<CoBlame, Long> {

}


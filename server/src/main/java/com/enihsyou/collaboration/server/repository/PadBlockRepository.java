package com.enihsyou.collaboration.server.repository;

import com.enihsyou.collaboration.server.domain.CoPadControlBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 对{@link CoPadControlBlock}数据库表操作的DAO
 */@Repository
public interface PadBlockRepository extends JpaRepository<CoPadControlBlock, Long> {


}

package com.enihsyou.collaboration.server.repository;

import com.enihsyou.collaboration.server.domain.CoPad;
import com.enihsyou.collaboration.server.pojo.PadNotExistException;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 对{@link CoPad}数据库表操作的DAO
 */@Repository
public interface PadRepository extends JpaRepository<CoPad, Long> {


    /** 用id搜索 */
    @NotNull
    default CoPad queryById(final long padId) {
        return findById(padId).orElseThrow(() -> new PadNotExistException(padId));
    }
}


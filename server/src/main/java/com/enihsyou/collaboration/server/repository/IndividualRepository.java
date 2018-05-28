package com.enihsyou.collaboration.server.repository;

import com.enihsyou.collaboration.server.domain.CoIndividual;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 对{@link CoIndividual}数据库表操作的DAO
 */@Repository
public interface IndividualRepository extends JpaRepository<CoIndividual, Long> {

    /** 用用户名搜索 */
    @Nullable
    CoIndividual findByUsername(@NotNull final String username);
}

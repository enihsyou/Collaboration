package com.enihsyou.collaboration.server.repository;

import com.enihsyou.collaboration.server.domain.CoIndividual;
import com.enihsyou.collaboration.server.pojo.UserNotExistException;
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
    CoIndividual findByUsername(@NotNull  String username);

    @NotNull
    default CoIndividual getUser(@NotNull String username){
        final CoIndividual individual = findByUsername(username);
        if (individual != null) return individual;
        else throw new UserNotExistException(username);
    }
}

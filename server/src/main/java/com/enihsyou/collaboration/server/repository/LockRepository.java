package com.enihsyou.collaboration.server.repository;

import com.enihsyou.collaboration.server.domain.CoIndividual;
import com.enihsyou.collaboration.server.domain.CoLock;
import com.enihsyou.collaboration.server.pojo.LockNotExistException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LockRepository extends JpaRepository<CoLock, Long> {

    @Nullable
    CoLock queryByBelongTo(@NotNull final CoIndividual who);

    @NotNull
    default CoLock queryById(@NotNull final Long lockId) {
        return findById(lockId).orElseThrow(() -> new LockNotExistException(lockId));
    }
}

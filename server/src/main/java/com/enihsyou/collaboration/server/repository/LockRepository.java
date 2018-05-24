package com.enihsyou.collaboration.server.repository;

import com.enihsyou.collaboration.server.domain.CoLock;
import com.enihsyou.collaboration.server.pojo.LockNotExistException;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LockRepository extends JpaRepository<CoLock, Long> {

    /**用id搜索*/
    @NotNull
    default CoLock queryById(@NotNull final Long lockId) {
        return findById(lockId).orElseThrow(() -> new LockNotExistException(lockId));
    }
}

package com.enihsyou.collaboration.server.repository;

import com.enihsyou.collaboration.server.domain.CoCabinet;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CabinetRepository extends JpaRepository<CoCabinet, Long> {

    @Query("select c from CoCabinet c where c.id=?1 and c.belongTo.username=?2")
    boolean isByIdBelongToUsername(@NotNull final Long id, @NotNull final String username);
}

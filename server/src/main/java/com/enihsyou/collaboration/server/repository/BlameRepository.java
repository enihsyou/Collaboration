package com.enihsyou.collaboration.server.repository;

import com.enihsyou.collaboration.server.domain.CoBlame;
import com.enihsyou.collaboration.server.domain.CoIndividual;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlameRepository extends JpaRepository<CoBlame, Long> {

    @Nullable
    CoBlame queryByBelongTo(@NotNull final CoIndividual who);
}


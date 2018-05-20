package com.enihsyou.collaboration.server.repository;

import com.enihsyou.collaboration.server.domain.CoIndividual;
import com.enihsyou.collaboration.server.domain.CoPad;
import com.enihsyou.collaboration.server.pojo.PadNotExistException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PadRepository extends JpaRepository<CoPad, Long> {

    @Nullable
    CoPad queryByBelongTo(@NotNull final CoIndividual who);


    @NotNull
    default CoPad queryById(final long padId) {
        return findById(padId).orElseThrow(() -> new PadNotExistException(padId));
    }
}


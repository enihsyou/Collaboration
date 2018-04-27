package com.enihsyou.collaboration.server.repository;

import com.enihsyou.collaboration.server.domain.CoBlame;
import com.enihsyou.collaboration.server.domain.CoIndividual;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlameRepository extends JpaRepository<CoBlame, Long> {

    CoBlame queryByBelongTo(CoIndividual belongTo);
}


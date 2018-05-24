package com.enihsyou.collaboration.server.repository;

import com.enihsyou.collaboration.server.domain.CoBlame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlameRepository extends JpaRepository<CoBlame, Long> {

}


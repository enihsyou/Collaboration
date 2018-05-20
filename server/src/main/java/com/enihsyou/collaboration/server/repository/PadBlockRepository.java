package com.enihsyou.collaboration.server.repository;

import com.enihsyou.collaboration.server.domain.CoPadControlBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PadBlockRepository extends JpaRepository<CoPadControlBlock, Long> {


}

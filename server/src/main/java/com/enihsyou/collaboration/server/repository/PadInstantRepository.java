package com.enihsyou.collaboration.server.repository;

import com.enihsyou.collaboration.server.domain.CoPadInstant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PadInstantRepository extends JpaRepository<CoPadInstant, Long> {

}

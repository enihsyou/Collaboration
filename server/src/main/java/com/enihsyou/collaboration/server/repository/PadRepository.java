package com.enihsyou.collaboration.server.repository;

import com.enihsyou.collaboration.server.domain.CoPad;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PadRepository extends JpaRepository<CoPad, Long> {}

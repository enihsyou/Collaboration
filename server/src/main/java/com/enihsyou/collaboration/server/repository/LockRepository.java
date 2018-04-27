package com.enihsyou.collaboration.server.repository;

import com.enihsyou.collaboration.server.domain.CoLock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LockRepository extends JpaRepository<CoLock, Long> {}

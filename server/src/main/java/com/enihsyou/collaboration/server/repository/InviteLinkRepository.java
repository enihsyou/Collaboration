package com.enihsyou.collaboration.server.repository;

import com.enihsyou.collaboration.server.domain.CoInviteLink;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InviteLinkRepository extends JpaRepository<CoInviteLink, String> {}

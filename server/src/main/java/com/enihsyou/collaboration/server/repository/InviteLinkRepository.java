package com.enihsyou.collaboration.server.repository;

import com.enihsyou.collaboration.server.domain.CoInviteLink;
import com.enihsyou.collaboration.server.pojo.InviteLinkNotExistException;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InviteLinkRepository extends JpaRepository<CoInviteLink, String> {

    @NotNull
    default CoInviteLink queryByToken(final String token) {
        return findById(token).orElseThrow(() -> new InviteLinkNotExistException(token));
    }
}

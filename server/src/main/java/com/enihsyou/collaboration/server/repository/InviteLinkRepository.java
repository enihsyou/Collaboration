package com.enihsyou.collaboration.server.repository;

import com.enihsyou.collaboration.server.domain.CoInviteLink;
import com.enihsyou.collaboration.server.domain.CoPad;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InviteLinkRepository extends JpaRepository<CoInviteLink, String> {

    @Nullable
    CoInviteLink queryByInvitee(String who);

    @Nullable
    CoInviteLink queryByPad(CoPad which);
}

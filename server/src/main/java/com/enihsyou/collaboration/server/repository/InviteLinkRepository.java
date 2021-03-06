package com.enihsyou.collaboration.server.repository;

import com.enihsyou.collaboration.server.domain.CoInviteLink;
import com.enihsyou.collaboration.server.pojo.InviteLinkNotExistException;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 对{@link CoInviteLink}数据库表操作的DAO
 */@Repository
public interface InviteLinkRepository extends JpaRepository<CoInviteLink, String> {

     Optional<CoInviteLink> findByToken(String token);

    /** 用id搜索 */
    @NotNull
    default CoInviteLink queryByToken(final String token) {
        return findByToken(token).orElseThrow(() -> new InviteLinkNotExistException(token));
    }
}

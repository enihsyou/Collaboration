package com.enihsyou.collaboration.server.domain;

import kotlin.NotImplementedError;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * 邀请链接
 * 通过[CoInviteLink.inviter]邀请[CoInviteLink.invitee]加入文档[CoInviteLink.pad]，并授予[CoInviteLink.permission]权限
 * 如果多次邀请同一个被邀请者，应该返回同一个邀请码
 */
@Entity
public class CoInviteLink {

    /** 发给被邀请用户的授权令牌，用户使用这个串确认邀请 */
    @Id
    @NotNull
    private String token = "";

    /** 被邀请的人 */
    @Nullable
    private String invitee;

    /** 被邀请者加入的文稿[CoPad] */
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private CoPad pad = CoPad.DUMMY;

    /** 被邀请者被授予的权限 */
    @Enumerated(EnumType.STRING)
    @NotNull
    private CoLinkStatus permission = CoLinkStatus.REVOKED;

    /** 这个邀请链接的创建时间 */
    @NotNull
    private Instant createdTime = Instant.now();

    /** 这个邀请链接过期的时间，重复邀请会延长寿命 */
    @NotNull
    private Instant expiredTime = createdTime.plus(1, ChronoUnit.DAYS);


    ////
    // Functions
    ////

    public boolean isTargeted(String username) {
        if (invitee == null) return true;
        return Objects.equals(invitee, username);
    }

    public boolean isExpired() {
        return permission == CoLinkStatus.REVOKED || expiredTime.isAfter(Instant.now());
    }


    ////
    // Getter Setter
    ////

    /** 发起邀请的人 */
    public CoIndividual getInviter() {
        throw new NotImplementedError();
    }

    @NotNull
    public String getToken() {
        return token;
    }

    public CoInviteLink setToken(@NotNull final String token) {
        this.token = token;
        return this;
    }

    @Nullable
    public String getInvitee() {
        return invitee;
    }

    public CoInviteLink setInvitee(@NotNull final String invitee) {
        this.invitee = invitee;
        return this;
    }

    @NotNull
    public CoPad getPad() {
        return pad;
    }

    public CoInviteLink setPad(@NotNull final CoPad pad) {
        this.pad = pad;
        return this;
    }

    @NotNull
    public CoLinkStatus getPermission() {
        return permission;
    }

    public CoInviteLink setPermission(@NotNull final CoLinkStatus permission) {
        this.permission = permission;
        return this;
    }

    @NotNull
    public LocalDateTime getCreatedTime() {
        return LocalDateTime.from(createdTime);
    }

    @NotNull
    public LocalDateTime getExpiredTime() {
        return LocalDateTime.from(expiredTime);
    }
}

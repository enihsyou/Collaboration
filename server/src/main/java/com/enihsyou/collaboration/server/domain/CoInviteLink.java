package com.enihsyou.collaboration.server.domain;

import kotlin.NotImplementedError;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

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
    @NotNull private String invitee = "";

    /** 被邀请者加入的文稿[CoPad] */
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private CoPad pad = CoPad.DUMMY;

    /** 被邀请者被授予的权限 */
    @NotNull private CoLinkStatus permission = CoLinkStatus.REVOKED;

    /** 这个邀请链接的创建时间 */
    @NotNull private LocalDateTime createdTime = LocalDateTime.now();

    /** 这个邀请链接过期的时间，重复邀请会延长寿命 */
    @NotNull private LocalDateTime expiredTime = createdTime.plusDays(1);

    ////
    // Constructors
    ////

    public CoInviteLink() {
    }

    public CoInviteLink(
        @NotNull final String token,
        @NotNull final String invitee,
        @NotNull final CoPad pad,
        @NotNull final CoLinkStatus permission) {
        this.token = token;
        this.invitee = invitee;
        this.pad = pad;
        this.permission = permission;
    }

    ////
    // Getter Setter
    ////

    /** 发起邀请的人 */
    public CoCabinet getInviter() {
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

    @NotNull
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
        return createdTime;
    }

    public CoInviteLink setCreatedTime(@NotNull final LocalDateTime createdTime) {
        this.createdTime = createdTime;
        return this;
    }

    @NotNull
    public LocalDateTime getExpiredTime() {
        return expiredTime;
    }

    public CoInviteLink setExpiredTime(@NotNull final LocalDateTime expiredTime) {
        this.expiredTime = expiredTime;
        return this;
    }
}

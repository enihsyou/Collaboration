package com.enihsyou.collaboration.server.domain;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.util.ClassUtils;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * 邀请链接
 * 通过[CoInviteLink.inviter]邀请[CoInviteLink.invitee]加入文档[CoInviteLink.pad]，并授予[CoInviteLink.permission]权限
 * 如果多次邀请同一个被邀请者，应该返回同一个邀请码
 */
@Entity
public class CoInviteLink implements Persistable<String> {

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

    /** 测试给定的用户名是否是这个邀请链接允许的用户 */
    public boolean isTargeted(String username) {
        if (invitee == null) return true;
        return Objects.equals(invitee, username);
    }

    /** 测试这个邀请链接有没有过期 */
    public boolean isExpired() {
        return permission == CoLinkStatus.REVOKED || expiredTime.isAfter(Instant.now());
    }

    /** 发起邀请的人 */
    public CoIndividual getInviter() {
        return pad.getBelongTo();
    }

    ////
    // Getter Setter
    ////

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

    public CoInviteLink setInvitee(@Nullable final String invitee) {
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
        return LocalDateTime.ofInstant(createdTime, ZoneId.systemDefault());
    }

    @NotNull
    public LocalDateTime getExpiredTime() {
        return LocalDateTime.ofInstant(expiredTime, ZoneId.systemDefault());
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {

        int hashCode = 17;

        hashCode += getId().hashCode() * 31;

        return hashCode;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {

        if (null == obj) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        if (!getClass().equals(ClassUtils.getUserClass(obj))) {
            return false;
        }

        AbstractPersistable<?> that = (AbstractPersistable<?>) obj;

        return this.getId().equals(that.getId());
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("Entity of type %s with id: %s", this.getClass().getName(), getId());
    }

    @Override
    @NotNull
    public String getId() {
        return token;
    }

    @Override
    public boolean isNew() {
        return token.isEmpty();
    }
}

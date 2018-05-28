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
 * 邀请链接。
 * <p>
 * 通过邀请者邀请被邀请者加入文档，并授予一定的权限。
 * 或者创建一个不限定被邀请者的邀请链接。
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

    /** 获取邀请令牌 */
    @NotNull
    public String getToken() {
        return token;
    }

    /** 设置邀请令牌 */
    public CoInviteLink setToken(@NotNull final String token) {
        this.token = token;
        return this;
    }

    /** 获取被邀请者 */
    @Nullable
    public String getInvitee() {
        return invitee;
    }

    /** 设置被邀请者 */
    public CoInviteLink setInvitee(@Nullable final String invitee) {
        this.invitee = invitee;
        return this;
    }

    /** 获取邀请加入的文稿 */
    @NotNull
    public CoPad getPad() {
        return pad;
    }

    /** 设置邀请加入的文稿 */
    public CoInviteLink setPad(@NotNull final CoPad pad) {
        this.pad = pad;
        return this;
    }

    /** 获取邀请设置的权限 */
    @NotNull
    public CoLinkStatus getPermission() {
        return permission;
    }

    /** 设置邀请设置的权限 */
    public CoInviteLink setPermission(@NotNull final CoLinkStatus permission) {
        this.permission = permission;
        return this;
    }

    /** 获取创建时间 */
    @NotNull
    public LocalDateTime getCreatedTime() {
        return LocalDateTime.ofInstant(createdTime, ZoneId.systemDefault());
    }

    /** 获取过期时间 */
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

    /** 获取JPA ID */
    @Override
    @NotNull
    public String getId() {
        return token;
    }

    /** JPA新创建元素判定 */
    @Override
    public boolean isNew() {
        return token.isEmpty();
    }
}

package com.enihsyou.collaboration.server.domain;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import java.time.Instant;

/** 代表每个用户的具体信息 */
@Entity
public class CoIndividual extends AbstractPersistable<Long> {

    static final CoIndividual DUMMY = new CoIndividual();

    /** 用户注册的时间 */
    @NotNull
    private final Instant createdTime = Instant.now();

    /** 用户登录名 */
    @NotNull
    private String username = "";

    /** 用户密码，禁止保存明文 */
    @NotNull
    private String password = "";

    /** 用户邮箱地址，用来找回密码 */
    @Nullable
    private String emailAddress;

    /** 用户最后登录的时间 */
    @Nullable
    private Instant lastLoginTime;

    /** 用户的文件柜 */
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @NotNull
    private CoCabinet cabinet = CoCabinet.DUMMY;

    /** 找回密码的凭据 */
    @Nullable
    private String resetPasswordToken;
    ////
    // Getter Setter
    ////

    @NotNull
    public String getUsername() {
        return username;
    }

    public CoIndividual setUsername(@NotNull final String username) {
        this.username = username;
        return this;
    }

    @NotNull
    public String getPassword() {
        return password;
    }

    public CoIndividual setPassword(@NotNull final String password) {
        this.password = password;
        return this;
    }

    @Nullable
    public String getEmailAddress() {
        return emailAddress;
    }

    public CoIndividual setEmailAddress(@Nullable final String emailAddress) {
        this.emailAddress = emailAddress;
        return this;
    }

    @Nullable
    public Instant getLastLoginTime() {
        return lastLoginTime;
    }

    public CoIndividual setLastLoginTime(@Nullable final Instant lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
        return this;
    }

    @NotNull
    public Instant getCreatedTime() {
        return createdTime;
    }

    @NotNull
    public CoCabinet getCabinet() {
        return cabinet;
    }

    public CoIndividual setCabinet(@NotNull final CoCabinet cabinet) {
        this.cabinet = cabinet;
        return this;
    }

    @Nullable
    public String getResetPasswordToken() {
        return resetPasswordToken;
    }

    public CoIndividual setResetPasswordToken(@Nullable final String resetPasswordToken) {
        this.resetPasswordToken = resetPasswordToken;
        return this;
    }
}


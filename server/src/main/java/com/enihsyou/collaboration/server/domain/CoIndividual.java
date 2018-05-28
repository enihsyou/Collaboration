package com.enihsyou.collaboration.server.domain;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;

import static java.time.Instant.now;

/** 代表每个用户的具体信息 */
@Entity
public class CoIndividual extends AbstractPersistable<Long> {

    static final CoIndividual DUMMY = new CoIndividual();

    /** 用户注册的时间 */
    @NotNull
    private final Instant createdTime = now();

    /** 用户登录名 */
    @NotNull
    private String username = "";

    /** 用户密码，禁止保存明文 */
    @NotNull
    private String password = "";

    /** 用户邮箱地址，用来找回密码 */
    @Nullable
    private String emailAddress;

    /**
     * 用户的文件柜里面存有的文档集合
     * 用来存放用户拥有的所有文档，包括创建的和被邀请合作的
     */
    @OneToMany(mappedBy = "individual", orphanRemoval = true)
    @NotNull
    private Set<CoPadControlBlock> pads = new HashSet<>();

    /** 找回密码的凭据 */
    @Nullable
    private String resetPasswordToken;

    ////
    // Functions
    ////

    /** 添加文稿 */
    public CoIndividual addPad(@NotNull final CoPadControlBlock pads) {
        this.pads.add(pads);
        return this;
    }
    ////
    // Getter Setter
    ////

    /** 获取用户名 */
    @NotNull
    public String getUsername() {
        return username;
    }

    /** 设置用户名 */
    public CoIndividual setUsername(@NotNull final String username) {
        this.username = username;
        return this;
    }

    /** 获取加密后的密码 */
    @NotNull
    public String getPassword() {
        return password;
    }

    /** 设置加密后的密码 */
    public CoIndividual setPassword(@NotNull final String password) {
        this.password = password;
        return this;
    }

    /** 获取用户邮箱 */
    @Nullable
    public String getEmailAddress() {
        return emailAddress;
    }

    /** 设置用户邮箱 */
    public CoIndividual setEmailAddress(@Nullable final String emailAddress) {
        this.emailAddress = emailAddress;
        return this;
    }

    /** 获取用户所有的文稿 */
    @NotNull
    public Set<CoPadControlBlock> getPads() {
        return pads;
    }

    /** 获取用户创建时间 */
    @NotNull
    public LocalDateTime getCreatedTime() {
        return LocalDateTime.ofInstant(createdTime, ZoneId.systemDefault());
    }

    /** 获取找回密码令牌 */
    @Nullable
    public String getResetPasswordToken() {
        return resetPasswordToken;
    }

    /** 设置找回密码令牌 */
    public CoIndividual setResetPasswordToken(@Nullable final String resetPasswordToken) {
        this.resetPasswordToken = resetPasswordToken;
        return this;
    }
}


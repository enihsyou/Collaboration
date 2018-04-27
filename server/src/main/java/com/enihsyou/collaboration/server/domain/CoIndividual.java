package com.enihsyou.collaboration.server.domain;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;

/** 代表每个用户的具体信息 */
@Entity
public class CoIndividual {

    static final CoIndividual DUMMY = new CoIndividual();

    /** 用户注册的时间 */
    @NotNull private final LocalDateTime createdTime = LocalDateTime.now();

    @Id
    @GeneratedValue
    private long id;

    /** 用户登录名 */
    @NotNull private String username = "";

    /** 用户密码，禁止保存明文 */
    @NotNull private String password = "";

    /** 用户最后登录的时间 */
    @Nullable private LocalDateTime lastLoginTime;

    ////
    // Constructors
    ////

    @OneToOne(mappedBy = "belongTo", fetch = FetchType.LAZY, orphanRemoval = true)
    @NotNull
    private CoCabinet cabinet = CoCabinet.DUMMY;

    public CoIndividual() {
    }

    ////
    // Getter Setter
    ////

    public CoIndividual(@NotNull final String username, @NotNull final String password) {
        this.username = username;
        this.password = password;
    }

    public long getId() {
        return id;
    }

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
    public LocalDateTime getLastLoginTime() {
        return lastLoginTime;
    }

    public CoIndividual setLastLoginTime(@Nullable final LocalDateTime lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
        return this;
    }

    @NotNull
    public LocalDateTime getCreatedTime() {
        return createdTime;
    }
}


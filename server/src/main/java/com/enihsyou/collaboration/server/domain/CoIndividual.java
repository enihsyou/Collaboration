package com.enihsyou.collaboration.server.domain;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.jpa.domain.AbstractPersistable;

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

    /** 用户最后登录的时间 */
    @Nullable
    private Instant lastLoginTime;

    /** 用户的文件柜*/
    @OneToOne(fetch = FetchType.LAZY)
    @NotNull
    private CoCabinet cabinet = CoCabinet.DUMMY;

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

}


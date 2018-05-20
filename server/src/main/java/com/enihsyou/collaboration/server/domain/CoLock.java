package com.enihsyou.collaboration.server.domain;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.Instant;
import java.time.LocalDateTime;

import static com.enihsyou.collaboration.server.domain.CoIndividual.DUMMY;
import static java.time.Instant.now;
import static javax.persistence.FetchType.LAZY;

/** 一段被🔒锁定的文字范围 */
@Entity
public class CoLock extends AbstractPersistable<Long> {

    /** 拥有者 */
    @ManyToOne(fetch = LAZY)
    @NotNull
    private CoIndividual belongTo = DUMMY;

    /** 左端点，包含 */
    private int left;

    /** 右端点，不包含 */
    private int right;

    /** 锁定时间（创建时间） */
    @NotNull
    private Instant createdTime = now();

    ////
    // Getter Setter
    ////

    @NotNull
    public CoIndividual getBelongTo() {
        return belongTo;
    }

    public CoLock setBelongTo(@NotNull final CoIndividual belongTo) {
        this.belongTo = belongTo;
        return this;
    }

    public int getLeft() {
        return left;
    }

    public CoLock setLeft(final int left) {
        this.left = left;
        return this;
    }

    public int getRight() {
        return right;
    }

    public CoLock setRight(final int right) {
        this.right = right;
        return this;
    }

    @NotNull
    public LocalDateTime getCreatedTime() {
        return LocalDateTime.from(createdTime);
    }

    /** 当前🔒是否已过期 */
    public boolean isExpired() {
        return now().isAfter(createdTime);
    }
}


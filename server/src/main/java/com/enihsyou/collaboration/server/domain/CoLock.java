package com.enihsyou.collaboration.server.domain;

import kotlin.ranges.IntRange;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static java.time.Instant.now;
import static javax.persistence.FetchType.LAZY;

/** 一段被🔒锁定的文字范围 */
@Entity
public class CoLock extends AbstractPersistable<Long> {

    /** 执行锁定的人 */
    @ManyToOne(fetch = LAZY)
    @NotNull
    private CoIndividual belongTo = CoIndividual.DUMMY;

    /** 锁定的文稿 */
    @ManyToOne(fetch = LAZY)
    @NotNull
    private CoPad pad = CoPad.DUMMY;

    /** 左端点，包含 */
    private int left;

    /** 右端点，不包含 */
    private int right;

    /** 锁定时间（创建时间） */
    @NotNull
    private Instant createdTime = now();

    ////
    // Functions
    ////

    @NotNull
    public IntRange getRange() {
        return new IntRange(left, right);
    }

    public int getLeft() {
        return left;
    }

    public int getRight() {
        return right;
    }

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

    @NotNull
    public CoPad getPad() {
        return pad;
    }

    public CoLock setPad(@NotNull final CoPad pad) {
        this.pad = pad;
        return this;
    }


    @NotNull
    public LocalDateTime getCreatedTime() {
        return LocalDateTime.ofInstant(createdTime, ZoneId.systemDefault());
    }

    /** 当前🔒是否已过期 */
    public boolean isExpired() {
        return now().isAfter(createdTime);
    }
}


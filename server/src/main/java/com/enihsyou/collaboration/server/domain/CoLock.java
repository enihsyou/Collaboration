package com.enihsyou.collaboration.server.domain;

import kotlin.ranges.IntRange;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static javax.persistence.FetchType.LAZY;

/**
 * 一段被🔒锁定的文字范围
 * e.g. "abcdef" 0..0
 */
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

    /**
     * 左端点，包含
     * left_inclusive
     * 第一个被包含的字符位置
     */
    private int left;

    /**
     * 右端点，不包含
     * right_exclusive
     * 在这个位置及其之后的字符，都不在这个🔒的锁定范围内
     */
    private int right;

    /** 锁定时间（创建时间） */
    @NotNull
    private Instant createdTime = Instant.now();

    ////
    // Functions
    ////

    @NotNull
    public CoIndividual getLocker() {
        return belongTo;
    }

    public CoLock setLocker(@NotNull final CoIndividual belongTo) {
        this.belongTo = belongTo;
        return this;
    }

    @NotNull
    public IntRange getRange() {
        return new IntRange(left, right);
    }

    @NotNull
    public CoLock setRange(@NotNull IntRange range) {
        this.left = range.getFirst();
        this.right = range.getLast();
        return this;
    }

    public int getLeft() {
        return left;
    }

    public int getRight() {
        return right;
    }

    public boolean isInsert() {
        return left == right;
    }
    ////
    // Getter Setter
    ////


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
        return Instant.now().isAfter(createdTime);
    }
}


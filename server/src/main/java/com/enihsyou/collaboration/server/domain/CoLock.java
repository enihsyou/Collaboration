package com.enihsyou.collaboration.server.domain;

import kotlin.ranges.IntRange;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import static javax.persistence.FetchType.LAZY;

/**
 * 一段被🔒锁定的文字范围
 * <p>
 * 坐标位置如下
 * <pre>{@code
 *  a b c d e f
 * 0 1 2 3 4 5 6
 * }</pre>
 * 左端点小于等于右端点数字
 * e.g. <p>
 * "abcdef" 0..0 -> a字符的前面<p>
 * "abcdef" 0..1 -> a字符<p>
 * "abcdef" 0..6 -> abcdef全部<p>
 * "abcdef" 3..5 -> de全部
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

    /** 获取执行锁定的人 */
    @NotNull
    public CoIndividual getLocker() {
        return belongTo;
    }

    /** 设置执行锁定的人 */
    public CoLock setLocker(@NotNull final CoIndividual belongTo) {
        this.belongTo = belongTo;
        return this;
    }

    /** 获取锁定范围 */
    @NotNull
    public IntRange getRange() {
        return new IntRange(left, right);
    }

    /** 设置锁定范围 */
    @NotNull
    public CoLock setRange(@NotNull IntRange range) {
        this.left = range.getFirst();
        this.right = range.getLast();
        return this;
    }

    /** 获取锁定左端点 */
    public int getLeft() {
        return left;
    }

    /** 获取锁定右端点 */
    public int getRight() {
        return right;
    }

    /**
     * 判断这个锁是不是插入锁
     * 也就是锁定范围长度为0
     */
    public boolean isInsert() {
        return left == right;
    }

    /**
     * 当前🔒是否已过期
     * 有效时长6小时
     */
    public boolean isExpired() {
        return Instant.now().isAfter(createdTime.plus(6, ChronoUnit.HOURS));
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
}


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


/** 一段属于用户贡献的位置范围 */
@Entity
public class CoBlame extends AbstractPersistable<Long> {

    /** 拥有者，也就是创建这个贡献的用户 */
    @ManyToOne(fetch = LAZY)
    @NotNull
    private CoIndividual belongTo = CoIndividual.DUMMY;

    /** 贡献发生的文稿 */
    @ManyToOne(fetch = LAZY)
    @NotNull
    private CoPad pad = CoPad.DUMMY;

    /**
     * 左端点，包含
     * left_inclusive
     */
    private int left;

    /**
     * 右端点，不包含
     * right_exclusive
     */
    private int right;

    /** 贡献时间（创建时间） */
    @NotNull
    private Instant createdTime = Instant.now();

    ////
    // Functions
    ////

    /** 使用 {@link CoLock} 创建对象的工厂方法 */
    @NotNull
    public static CoBlame from(@NotNull final CoLock lock) {
        return new CoBlame().setContributor(lock.getLocker()).setPad(lock.getPad()).setRange(lock.getRange());
    }

    /**
     * 重新计算右端点的位置。
     *
     * 用在用户使用锁替换了一段文字之后，左端点长度不变，右端点会变化。
     *
     * @param replacement 区间内新替换的内容
     */
    @NotNull
    public CoBlame recalculateUsingReplacement(@NotNull final String replacement) {
        right = left + replacement.length();
        return this;
    }

    /** 获取贡献范围 */
    @NotNull
    public IntRange getRange() {
        return new IntRange(left, right);
    }

    /** 设置贡献范围 */
    @NotNull
    public CoBlame setRange(@NotNull IntRange range) {
        this.left = range.getFirst();
        this.right = range.getLast();
        return this;
    }

    /** 获取贡献左端点 */
    public int getLeft() {
        return left;
    }

    /** 获取贡献右端点 */
    public int getRight() {
        return right;
    }

    /** 获取贡献人 */
    @NotNull
    public CoIndividual getContributor() {
        return belongTo;
    }

    /** 设置贡献人 */
    private CoBlame setContributor(@NotNull final CoIndividual belongTo) {
        this.belongTo = belongTo;
        return this;
    }

    ////
    // Getter Setter
    ////

    @NotNull
    public CoPad getPad() {
        return pad;
    }

    private CoBlame setPad(@NotNull final CoPad pad) {
        this.pad = pad;
        return this;
    }

    @NotNull
    public LocalDateTime getCreatedTime() {
        return LocalDateTime.ofInstant(createdTime, ZoneId.systemDefault());
    }
}

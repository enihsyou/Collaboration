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

    /** 拥有者 */
    @ManyToOne(fetch = LAZY)
    @NotNull
    private CoIndividual belongTo = CoIndividual.DUMMY;

    /** 贡献的文稿 */
    @ManyToOne(fetch = LAZY)
    @NotNull
    private CoPad pad = CoPad.DUMMY;

    /** 左端点，包含 */
    private int left;

    /** 右端点，不包含 */
    private int right;

    /** 贡献时间（创建时间） */
    @NotNull
    private Instant createdTime = Instant.now();

    ////
    // Functions
    ////

    @NotNull
    public static CoBlame from(@NotNull final CoLock lock) {
        return new CoBlame()
            .setContributor(lock.getLocker())
            .setPad(lock.getPad())
            .setRange(lock.getRange());
    }

    @NotNull
    public CoBlame extendLength(final int howLong) {
        right += howLong;
        return this;
    }

    @NotNull
    public IntRange getRange() {
        return new IntRange(left, right);
    }

    @NotNull
    public CoBlame setRange(@NotNull IntRange range) {
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

    @NotNull
    public CoIndividual getContributor() {
        return belongTo;
    }

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

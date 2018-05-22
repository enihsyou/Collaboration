package com.enihsyou.collaboration.server.domain;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/** 一段属于用户贡献的位置范围 */
@Entity
public class CoBlame extends AbstractPersistable<Long> {

    /** 拥有者 */
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private CoIndividual belongTo = CoIndividual.DUMMY;

    /** 左端点，包含 */
    private int left;

    /** 右端点，不包含 */
    private int right;

    /** 贡献时间（创建时间） */
    @NotNull
    private Instant createdTime = Instant.now();

    ////
    // Getter Setter
    ////

    @NotNull
    public CoIndividual getBelongTo() {
        return belongTo;
    }

    public CoBlame setBelongTo(@NotNull final CoIndividual belongTo) {
        this.belongTo = belongTo;
        return this;
    }

    public int getLeft() {
        return left;
    }

    public CoBlame setLeft(final int left) {
        this.left = left;
        return this;
    }

    public int getRight() {
        return right;
    }

    public CoBlame setRight(final int right) {
        this.right = right;
        return this;
    }

    @NotNull
    public LocalDateTime getCreatedTime() {
       return LocalDateTime.ofInstant(createdTime, ZoneId.systemDefault());
    }
}

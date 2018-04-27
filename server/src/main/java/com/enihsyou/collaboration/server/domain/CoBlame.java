package com.enihsyou.collaboration.server.domain;

import org.jetbrains.annotations.NotNull;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/** 一段属于用户贡献的位置范围 */
@Entity
public class CoBlame {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private CoIndividual belongTo = CoIndividual.DUMMY;

    /** 左端点，包含 */
    private int left;

    /** 右端点，不包含 */
    private int right;

    ////
    // Constructors
    ////

    public CoBlame() {
    }

    public CoBlame(@NotNull final CoIndividual belongTo, final int left, final int right) {
        this.belongTo = belongTo;
        this.left = left;
        this.right = right;
    }

    ////
    // Getter Setter
    ////

    public long getId() {
        return id;
    }

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
}

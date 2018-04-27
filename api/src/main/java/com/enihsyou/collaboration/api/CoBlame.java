package com.enihsyou.collaboration.api;

import org.jetbrains.annotations.NotNull;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/** 一段属于用户贡献的位置范围 */
@Entity
public class CoBlame {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @NotNull
    private CoIndividual belongTo = new CoIndividual();

    private int from;

    private int to;

    ////
    // Constructors
    ////

    public CoBlame() {
    }

    public CoBlame(@NotNull final CoIndividual belongTo, final int from, final int to) {
        this.belongTo = belongTo;
        this.from = from;
        this.to = to;
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

    public int getFrom() {
        return from;
    }

    public CoBlame setFrom(final int from) {
        this.from = from;
        return this;
    }

    public int getTo() {
        return to;
    }

    public CoBlame setTo(final int to) {
        this.to = to;
        return this;
    }
}

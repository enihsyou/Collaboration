package com.enihsyou.collaboration.api;

import org.jetbrains.annotations.NotNull;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/** 一段被🔒锁定的文字范围 */
@Entity
public class CoLock {

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

    public CoLock() {
    }

    public CoLock(@NotNull final CoIndividual belongTo, final int from, final int to) {
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

    public CoLock setBelongTo(@NotNull final CoIndividual belongTo) {
        this.belongTo = belongTo;
        return this;
    }

    public int getFrom() {
        return from;
    }

    public CoLock setFrom(final int from) {
        this.from = from;
        return this;
    }

    public int getTo() {
        return to;
    }

    public CoLock setTo(final int to) {
        this.to = to;
        return this;
    }
}


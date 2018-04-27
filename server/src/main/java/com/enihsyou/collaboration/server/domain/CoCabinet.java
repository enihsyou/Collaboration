package com.enihsyou.collaboration.server.domain;

import org.jetbrains.annotations.NotNull;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

/**
 * 每个用户都有的"文件柜"
 * 用来存放用户拥有的所有文档，包括创建的和被邀请合作的
 */
@Entity
public class CoCabinet {

    @Id
    @GeneratedValue
    private long id;

    /** 拥有这个文件柜的用户 */
    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
    @NotNull
    private CoIndividual belongTo = CoIndividual.DUMMY;

    /** 文件柜里面存有的文档集合，包括被邀请加入的 */
    @OneToMany(mappedBy = "cabinet", fetch = FetchType.LAZY, orphanRemoval = true)
    @NotNull
    private Set<CoPadControlBlock> pads = Collections.emptySet();

    /** 文件柜的创建时间 */
    @NotNull
    private LocalDateTime createdTime = LocalDateTime.now();

    ////
    // Constructors
    ////

    public CoCabinet() {}

    public CoCabinet(@NotNull final CoIndividual belongTo) {
        this.belongTo = belongTo;
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

    public CoCabinet setBelongTo(@NotNull final CoIndividual belongTo) {
        this.belongTo = belongTo;
        return this;
    }

    @NotNull
    public Set<CoPadControlBlock> getPads() {
        return pads;
    }

    public CoCabinet setPads(@NotNull final Set<CoPadControlBlock> pads) {
        this.pads = pads;
        return this;
    }

    @NotNull
    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public CoCabinet setCreatedTime(@NotNull final LocalDateTime createdTime) {
        this.createdTime = createdTime;
        return this;
    }

    static final CoCabinet DUMMY = new CoCabinet();
}


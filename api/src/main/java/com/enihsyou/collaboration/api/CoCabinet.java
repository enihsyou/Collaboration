package com.enihsyou.collaboration.api;

import org.jetbrains.annotations.NotNull;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.HashSet;
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

    /** 标注这个文件柜属于哪个用户的 */
    @ManyToOne
    @NotNull
    private CoIndividual belongTo = new CoIndividual();

    /** 文件柜里面存有的文档集合，包括被邀请加入的 */
    @OneToMany
    private Set<CoPad> pads = new HashSet<>();

    /** 文件柜的创建时间 */
    private LocalDateTime createdTime;

    ////
    // Constructors
    ////

    public CoCabinet() {
    }

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

    public Set<CoPad> getPads() {
        return pads;
    }

    public CoCabinet setPads(final Set<CoPad> pads) {
        this.pads = pads;
        return this;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public CoCabinet setCreatedTime(final LocalDateTime createdTime) {
        this.createdTime = createdTime;
        return this;
    }
}


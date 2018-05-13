package com.enihsyou.collaboration.server.domain;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.HashSet;
import java.util.Set;

/**
 * 每个用户都有的"文件柜"
 * 用来存放用户拥有的所有文档，包括创建的和被邀请合作的
 */
@Entity
public class CoCabinet extends AbstractPersistable<Long> {

    static final CoCabinet DUMMY = new CoCabinet();

    /** 拥有这个文件柜的用户 */
    @OneToOne(mappedBy = "cabinet", fetch = FetchType.LAZY)
    @NotNull
    private CoIndividual belongTo = CoIndividual.DUMMY;

    /** 文件柜里面存有的文档集合，包括被邀请加入的 */
    @OneToMany(mappedBy = "cabinet", orphanRemoval = true)
    @NotNull
    private Set<CoPadControlBlock> pads = new HashSet<>();

    ////
    // Getter Setter
    ////

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
}


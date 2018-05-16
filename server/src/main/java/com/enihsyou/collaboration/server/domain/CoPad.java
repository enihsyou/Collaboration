package com.enihsyou.collaboration.server.domain;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;


/**
 * 独立的一篇文稿的当前状态
 * 包含文稿的所有历史状态
 * 不同用户可能会同时操作同一篇文稿
 */
@Entity
public class CoPad extends AbstractPersistable<Long> {

    static final CoPad DUMMY = new CoPad();

    /** 创建这个文稿的所有者的文件柜，具有对文稿管理的最高权限 */
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private CoCabinet belongTo = CoCabinet.DUMMY;

    /** 文稿标题 */
    @NotNull
    private String title = "";

    /** 拥有本文稿的其他文件柜，也就是协同合作参与人员 */
    @OneToMany(mappedBy = "pad", orphanRemoval = true)
    @NotNull
    private Set<CoPadControlBlock> cabinets = new HashSet<>();

    /** 文稿的每个历史状态 */
    @OneToMany(mappedBy = "belongTo", orphanRemoval = true)
    @NotNull
    private Set<CoPadInstant> instants = new HashSet<>();

    /**
     * 当前文章中的🔒，每一个都是互相不重叠的区间范围
     * 如果文档中存在未释放的🔒，则不能保存历史状态
     */
    @OneToMany(mappedBy = "belongTo", orphanRemoval = true)
    @NotNull
    private Set<CoLock> locks = new HashSet<>();

    /** 文稿创建的时间 */
    @NotNull
    private Instant createdTime = Instant.now();

    ////
    // Getter Setter
    ////

    @NotNull
    public Set<CoPadInstant> getInstants() {
        return instants;
    }

    public CoPad addInstants(@NotNull final CoPadInstant instants) {
        this.instants.add(instants);
        instants.setBelongTo(this);
        return this;
    }

    @NotNull
    public String getTitle() {
        return title;

    }

    public CoPad setTitle(@NotNull final String title) {
        this.title = title;
        return this;
    }

    @NotNull
    public Set<CoLock> getLocks() {
        return locks;
    }

    public CoPad addLocks(@NotNull final Set<CoLock> locks) {
        this.locks = locks;
        return this;
    }

    @NotNull
    public CoCabinet getBelongTo() {
        return belongTo;
    }

    public CoPad setBelongTo(@NotNull final CoCabinet belongTo) {
        this.belongTo = belongTo;
        return this;
    }

    @NotNull
    public Set<CoPadControlBlock> getCabinets() {
        return cabinets;
    }

    public CoPad addCabinets(@NotNull final Set<CoPadControlBlock> cabinets) {
        this.cabinets = cabinets;
        return this;
    }

    @NotNull
    public Instant getCreatedTime() {
        return createdTime;
    }
}

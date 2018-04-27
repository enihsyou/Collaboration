package com.enihsyou.collaboration.server.domain;

import org.jetbrains.annotations.NotNull;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * 独立的一篇文稿的当前状态
 * 包含文稿的所有历史状态
 * 不同用户可能会同时操作同一篇文稿
 */
@Entity
public class CoPad {

    @Id
    @GeneratedValue
    private long id;

    /** 创建本文稿的用户，具有对文稿管理的最高权限 */
    @OneToMany(mappedBy = "pad", fetch = FetchType.LAZY, orphanRemoval = true)
    @NotNull
    private Set<CoPadControlBlock> cabinets = Collections.emptySet();

    /** 文稿的每个历史状态 */
    @OneToMany(mappedBy = "belongTo", fetch = FetchType.LAZY, orphanRemoval = true)
    @NotNull
    private Set<CoPadInstant> instants = new HashSet<>();

    /** 文稿标题 */
    @NotNull
    private String title = "";

    /**
     * 当前文章中的🔒，每一个都是互相不重叠的区间范围
     * 如果文档中存在未释放的🔒，则不能保存历史状态
     */
    @OneToMany(mappedBy = "belongTo", fetch = FetchType.LAZY, orphanRemoval = true)
    @NotNull
    private Set<CoLock> locks = new HashSet<>();

    /** 用户注册的时间 */
    @NotNull
    private LocalDateTime createdTime = LocalDateTime.now();

    ////
    // Constructors
    ////

    public CoPad() {
    }

    public CoPad(@NotNull final String title) {
        this.title = title;
    }

    ////
    // Getter Setter
    ////

    public long getId() {
        return id;
    }

    @NotNull
    public Set<CoPadInstant> getInstants() {
        return instants;
    }

    public CoPad setInstants(@NotNull final Set<CoPadInstant> instants) {
        this.instants = instants;
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

    public CoPad setLocks(@NotNull final Set<CoLock> locks) {
        this.locks = locks;
        return this;
    }

    @NotNull
    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public CoPad setCreatedTime(@NotNull final LocalDateTime createdTime) {
        this.createdTime = createdTime;
        return this;
    }

    static final CoPad DUMMY = new CoPad();
}

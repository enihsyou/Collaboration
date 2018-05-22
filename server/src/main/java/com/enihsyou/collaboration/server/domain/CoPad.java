package com.enihsyou.collaboration.server.domain;

import org.hibernate.annotations.Type;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;

import static java.time.Instant.now;
import static javax.persistence.FetchType.LAZY;


/**
 * 独立的一篇文稿的当前状态
 * 包含文稿的所有历史状态
 * 不同用户可能会同时操作同一篇文稿
 */
@Entity
public class CoPad extends AbstractPersistable<Long> {

    static final CoPad DUMMY = new CoPad();

    /** 创建这个文稿的所有者，具有对文稿管理的最高权限 */
    @ManyToOne(fetch = LAZY)
    @NotNull
    private CoIndividual belongTo = CoIndividual.DUMMY;

    /** 文稿标题 */
    @NotNull
    private String title = "";

    /** 文稿当前内容 */
    @NotNull
    @Lob
    @Basic(fetch = LAZY)
    @Type(type = "org.hibernate.type.TextType")
    private String body = "";

    /** 拥有本文稿的其他文件柜，也就是协同合作参与人员 */
    @OneToMany(mappedBy = "pad", orphanRemoval = true)
    @NotNull
    private Set<CoPadControlBlock> workers = new HashSet<>();

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

    /** 文章中每个用户的贡献区间 */
    @OneToMany(mappedBy = "belongTo", orphanRemoval = true)
    @NotNull
    private Set<CoBlame> contributes = new HashSet<>();

    /** 文稿创建的时间 */
    @NotNull
    private Instant createdTime = now();

    ////
    // Functions
    ////

    /** 文稿是否处于锁定状态 */
    public boolean isLocked() {
        return !locks.isEmpty();
    }

    /** 添加一个历史记录 */
    public CoPad addInstants(@NotNull final CoPadInstant instants) {
        this.instants.add(instants);
        instants.setBelongTo(this);
        return this;
    }

    ////
    // Getter Setter
    ////

    @NotNull
    public Set<CoBlame> getContributes() {
        return contributes;
    }

    @NotNull
    public Set<CoPadInstant> getInstants() {
        return instants;
    }

    public CoPad setContributes(@NotNull final Set<CoBlame> contributes) {
        this.contributes = contributes;
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
    public String getBody() {
        return body;
    }

    public CoPad setBody(@NotNull final String body) {
        this.body = body;
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
    public CoIndividual getBelongTo() {
        return belongTo;
    }

    public CoPad setBelongTo(@NotNull final CoIndividual belongTo) {
        this.belongTo = belongTo;
        return this;
    }

    @NotNull
    public Set<CoPadControlBlock> getWorkers() {
        return workers;
    }

    public CoPad addCabinets(@NotNull final Set<CoPadControlBlock> cabinets) {
        this.workers = cabinets;
        return this;
    }

    @NotNull
    public LocalDateTime getCreatedTime() {
        return LocalDateTime.ofInstant(createdTime, ZoneId.systemDefault());
    }
}

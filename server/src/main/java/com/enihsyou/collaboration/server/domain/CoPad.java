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

    /** 最新的版本号 */
    @NotNull
    private Long lastRevision = 0L;

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
    @OneToMany(mappedBy = "pad", orphanRemoval = true, cascade = CascadeType.ALL)
    @NotNull
    private Set<CoPadControlBlock> workers = new HashSet<>();

    /** 文稿的每个历史状态 */
    @OneToMany(mappedBy = "belongTo", orphanRemoval = true, cascade = CascadeType.ALL)
    @NotNull
    private Set<CoPadInstant> instants = new HashSet<>();

    /**
     * 当前文章中的🔒，每一个都是互相不重叠的区间范围
     * 如果文档中存在未释放的🔒，则不能保存历史状态
     */
    @OneToMany(mappedBy = "pad", orphanRemoval = true, cascade = CascadeType.ALL)
    @NotNull
    private Set<CoLock> locks = new HashSet<>();

    /** 文章中每个用户的贡献区间 */
    @OneToMany(mappedBy = "pad", orphanRemoval = true, cascade = CascadeType.ALL)
    @NotNull
    private Set<CoBlame> contributes = new HashSet<>();

    @OneToOne(mappedBy = "pad", cascade = CascadeType.ALL)
    private CoInviteLink inviteLink;

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
    public CoPad addInstants(@NotNull final CoPadInstant instant) {
        this.instants.add(instant);
        instant.setBelongTo(this);
        return this;
    }

    /** 添加一个锁定记录 */
    public CoPad addLock(@NotNull final CoLock lock) {
        this.locks.add(lock);
        lock.setLocker(this.belongTo);
        return this;
    }

    /** 移除一个锁定记录 */
    public CoPad removeLock(@NotNull final CoLock lock) {
        this.locks.remove(lock);
        return this;
    }

    /** 添加一个贡献记录 */
    public CoPad addContribute(@NotNull final CoBlame contribute) {
        this.contributes.add(contribute);
        return this;
    }

    ////
    // Getter Setter
    ////

    public CoInviteLink getInviteLink() {
        return inviteLink;
    }

    public CoPad setInviteLink(CoInviteLink inviteLink) {
        this.inviteLink = inviteLink;
        return this;
    }

    /** 获取当前文稿的版本号 */
    @NotNull
    public Long getLastRevision() {
        return lastRevision;
    }

    /** 设置当前文稿的版本号 */
    public CoPad setLastRevision(@NotNull final Long lastRevision) {
        this.lastRevision = lastRevision;
        return this;
    }

    /** 获取当前文稿的贡献值信息 */
    @NotNull
    public Set<CoBlame> getContributes() {
        return contributes;
    }

    /** 获取当前文稿的历史版本 */
    @NotNull
    public Set<CoPadInstant> getInstants() {
        return instants;
    }

    /** 获取标题 */
    @NotNull
    public String getTitle() {
        return title;

    }

    /** 设置标题 */
    public CoPad setTitle(@NotNull final String title) {
        this.title = title;
        return this;
    }

    /** 获取文稿主体 */
    @NotNull
    public String getBody() {
        return body;
    }

    /** 设置文稿主体 */
    public CoPad setBody(@NotNull final String body) {
        this.body = body;
        return this;
    }

    /** 获取锁 */
    @NotNull
    public Set<CoLock> getLocks() {
        return locks;
    }

    /** 获取拥有者 */
    @NotNull
    public CoIndividual getBelongTo() {
        return belongTo;
    }

    /** 设置拥有者 */
    public CoPad setBelongTo(@NotNull final CoIndividual belongTo) {
        this.belongTo = belongTo;
        return this;
    }

    /** 获取参与者 */
    @NotNull
    public Set<CoPadControlBlock> getWorkers() {
        return workers;
    }

    /** 获取创建时间 */
    @NotNull
    public LocalDateTime getCreatedTime() {
        return LocalDateTime.ofInstant(createdTime, ZoneId.systemDefault());
    }
}

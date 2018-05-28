package com.enihsyou.collaboration.server.domain;

import org.hibernate.annotations.Type;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;

import static com.enihsyou.collaboration.server.domain.CoPad.DUMMY;
import static java.time.Instant.now;
import static javax.persistence.FetchType.LAZY;

/**
 * 独立的一篇文稿的瞬时状态
 * 只有在文稿不存在锁的时候 才能执行保存状态的操作
 * 所以这个对象表示的是，文稿的历史状态之一
 * 不同用户可能会同时操作同一篇文稿
 */
@Entity
public class CoPadInstant extends AbstractPersistable<Long> {

    /** 这个状态属于哪个文稿 */
    @ManyToOne(fetch = LAZY)
    @NotNull
    private CoPad belongTo = DUMMY;

    /**
     * 这个状态是被谁创建的
     * 如果是系统自动创建的，则此域为null
     */
    @ManyToOne(fetch = LAZY)
    @NotNull
    private CoIndividual createdBy = CoIndividual.DUMMY;

    /** 版本号 */
    @NotNull
    private Long revision = 0L;

    /** 为这个瞬时状态添加的标记 */
    @Nullable
    private String tag;

    /** 文稿主体 */
    @NotNull
    @Lob
    @Basic(fetch = LAZY)
    @Type(type = "org.hibernate.type.TextType")
    private String body = "";

    /** 文章中每个用户的贡献区间 */
    @OneToMany(mappedBy = "belongTo", orphanRemoval = true)
    @NotNull
    private Set<CoBlame> contributes = new HashSet<>();

    /** 文稿的创建时间 */
    @NotNull
    private Instant createdTime = now();

    ////
    // Getter Setter
    ////

    /**获取版本号*/
    @NotNull
    public Long getRevision() {
        return revision;
    }

    /**设置这个历史版本的版本号*/
    public CoPadInstant setRevision(@NotNull final Long revision) {
        this.revision = revision;
        return this;
    }

    /**获取属于的文稿*/
    @NotNull
    public CoPad getBelongTo() {
        return belongTo;
    }

    /**设置属于的文稿*/
    public CoPadInstant setBelongTo(@NotNull final CoPad belongTo) {
        this.belongTo = belongTo;
        return this;
    }

    /**获取历史记录的标签*/
    @Nullable
    public String getTag() {
        return tag;
    }


/**设置历史记录的标签*/    public CoPadInstant setTag(@Nullable final String tag) {
        this.tag = tag;
        return this;
    }

    /**获取历史记录的主体*/
    @NotNull
    public String getBody() {
        return body;
    }

    /**设置历史记录的主体*/
    public CoPadInstant setBody(@NotNull final String body) {
        this.body = body;
        return this;
    }

    /**获取贡献值信息*/
    @NotNull
    public Set<CoBlame> getContributes() {
        return contributes;
    }

    /**设置贡献值信息*/
    public CoPadInstant setContributes(@NotNull final Set<CoBlame> contributes) {
        this.contributes = contributes;
        return this;
    }
    /**获取创建时间*/
    @NotNull
    public LocalDateTime getCreatedTime() {
        return LocalDateTime.ofInstant(createdTime, ZoneId.systemDefault());
    }

    /**获取历史版本的创建者*/
    @NotNull
    public CoIndividual getCreatedBy() {
        return createdBy;
    }

    /**设置历史版本的创建者*/
    public CoPadInstant setCreatedBy(@NotNull final CoIndividual createdBy) {
        this.createdBy = createdBy;
        return this;
    }
}

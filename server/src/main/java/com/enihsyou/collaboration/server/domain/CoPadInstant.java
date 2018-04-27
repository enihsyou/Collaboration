package com.enihsyou.collaboration.server.domain;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * 独立的一篇文稿的瞬时状态
 * 只有在文稿不存在锁的时候 才能执行保存状态的操作
 * 所以这个对象表示的是，文稿的历史状态之一
 * 不同用户可能会同时操作同一篇文稿
 */
@Entity
public class CoPadInstant {

    @Id
    @GeneratedValue
    private long id;

    /** 这个状态属于哪个文稿 */
    @ManyToOne
    @NotNull
    private CoPad belongTo = CoPad.DUMMY;

    /** 为这个瞬时状态添加的标记 */
    @Nullable
    private String tag;

    /** 文稿标题 */
    @NotNull
    private String title = "";

    /** 文稿主体 */
    @NotNull
    private String body = "";

    /** 文章中每个用户的贡献区间 */
    @OneToMany(mappedBy = "belongTo", fetch = FetchType.LAZY, orphanRemoval = true)
    @NotNull
    private Set<CoBlame> contributes = new HashSet<>();

    /** 文稿的创建时间 */
    @NotNull
    private LocalDateTime createdTime = LocalDateTime.now();

    ////
    // Constructors
    ////

    public CoPadInstant() {
    }

    public CoPadInstant(@NotNull final CoPad belongTo, @NotNull final String title, @NotNull final String body) {
        this.belongTo = belongTo;
        this.title = title;
        this.body = body;
    }

    ////
    // Getter Setter
    ////

    public long getId() {
        return id;
    }

    @NotNull
    public CoPad getBelongTo() {
        return belongTo;
    }

    public CoPadInstant setBelongTo(@NotNull final CoPad belongTo) {
        this.belongTo = belongTo;
        return this;
    }

    @Nullable
    public String getTag() {
        return tag;
    }

    public CoPadInstant setTag(@Nullable final String tag) {
        this.tag = tag;
        return this;
    }

    @NotNull
    public String getTitle() {
        return title;
    }

    public CoPadInstant setTitle(@NotNull final String title) {
        this.title = title;
        return this;
    }

    @NotNull
    public String getBody() {
        return body;
    }

    public CoPadInstant setBody(@NotNull final String body) {
        this.body = body;
        return this;
    }

    @NotNull
    public Set<CoBlame> getContributes() {
        return contributes;
    }

    public CoPadInstant setContributes(@NotNull final Set<CoBlame> contributes) {
        this.contributes = contributes;
        return this;
    }

    @NotNull
    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public CoPadInstant setCreatedTime(@NotNull final LocalDateTime createdTime) {
        this.createdTime = createdTime;
        return this;
    }
}

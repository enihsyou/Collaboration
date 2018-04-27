package com.enihsyou.collaboration.api;

import com.enihsyou.collaboration.api.CoPadControlBlock.PK;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 代表文件柜[CoCabinet]和文稿[CoPad]之间链接的相关信息
 * 实际作用是将用户的文件柜和独立的文稿连接起来，
 * 这篇文稿可能是自己创建的，也可能是别人邀请用户加入的
 */
@Entity
@IdClass(PK.class)
public class CoPadControlBlock {

    /** 链接的一端：文件柜 */
    @Id
    @ManyToOne
    @NotNull
    private CoCabinet cabinet = new CoCabinet();

    /** 链接的另一端：文稿 */
    @Id
    @ManyToOne
    @NotNull
    private CoPad pad = new CoPad();

    /** 链接状态 */
    @NotNull
    private CoLinkStatus status = CoLinkStatus.REVOKED;

    /** 链接的创建时间 */
    @NotNull
    private LocalDateTime createdTime = LocalDateTime.now();

    public static class PK implements Serializable {

        long cabinetId;

        long padId;
    }

    ////
    // Constructors
    ////

    public CoPadControlBlock() {
    }

    public CoPadControlBlock(@NotNull final CoCabinet cabinet,
                             @NotNull final CoPad pad,
                             @NotNull final CoLinkStatus status) {
        this.cabinet = cabinet;
        this.pad = pad;
        this.status = status;
    }

    ////
    // Getter Setter
    ////

    @NotNull
    public CoCabinet getCabinet() {
        return cabinet;
    }

    public CoPadControlBlock setCabinet(@NotNull final CoCabinet cabinet) {
        this.cabinet = cabinet;
        return this;
    }

    @NotNull
    public CoPad getPad() {
        return pad;
    }

    public CoPadControlBlock setPad(@NotNull final CoPad pad) {
        this.pad = pad;
        return this;
    }

    @NotNull
    public CoLinkStatus getStatus() {
        return status;
    }

    public CoPadControlBlock setStatus(@NotNull final CoLinkStatus status) {
        this.status = status;
        return this;
    }

    @NotNull
    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public CoPadControlBlock setCreatedTime(@NotNull final LocalDateTime createdTime) {
        this.createdTime = createdTime;
        return this;
    }
}


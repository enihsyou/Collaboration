package com.enihsyou.collaboration.server.domain;

import com.enihsyou.collaboration.server.domain.CoPadControlBlock.PK;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.enihsyou.collaboration.server.domain.CoIndividual.DUMMY;
import static com.enihsyou.collaboration.server.domain.CoLinkStatus.REVOKED;
import static java.time.Instant.now;
import static java.util.Objects.hash;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;

/**
 * 代表文件柜[CoCabinet]和文稿[CoPad]之间链接的相关信息
 * 实际作用是将用户的文件柜和独立的文稿连接起来，
 * 这篇文稿可能是自己创建的，也可能是别人邀请用户加入的
 */
@Entity
@IdClass(PK.class)
public class CoPadControlBlock {

    /** 链接的一端：用户文件柜 */
    @Id
    @ManyToOne(fetch = LAZY)
    @NotNull
    private CoIndividual individual = DUMMY;

    /** 链接的另一端：文稿 */
    @Id
    @ManyToOne(fetch = LAZY)
    @NotNull
    private CoPad pad = CoPad.DUMMY;

    /** 链接状态 */
    @Enumerated(STRING)
    @NotNull
    private CoLinkStatus status = REVOKED;

    /** 链接的创建时间 */
    @NotNull
    private Instant createdTime = now();

    ////
    // Getter Setter
    ////

    @NotNull
    public CoIndividual getIndividual() {
        return individual;
    }

    public CoPadControlBlock setIndividual(@NotNull final CoIndividual individual) {
        this.individual = individual;
        individual.addPad(this);
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
        return LocalDateTime.from(createdTime);
    }

    @Override
    public int hashCode() {
        return hash(individual, pad);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final CoPadControlBlock that = (CoPadControlBlock) o;
        return Objects.equals(individual, that.individual) && Objects.equals(pad, that.pad);
    }

    public static class PK implements Serializable {

        Long individual;

        Long pad;

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final PK pk = (PK) o;
            return Objects.equals(individual, pk.individual) && Objects.equals(pad, pk.pad);
        }

        @Override
        public int hashCode() {

            return hash(individual, pad);
        }
    }
}


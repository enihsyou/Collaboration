package com.enihsyou.collaboration.server.domain

import com.enihsyou.collaboration.server.util.DetailLevel

fun CoIndividual.toCreateVO(): Any =
    mapOf(
        "account_id" to id,
        "username" to username
    )

fun CoIndividual.toLoginVO(): Any =
    toDetailVO(DetailLevel.BRIEF)

fun CoIndividual.toDetailVO(level: DetailLevel = DetailLevel.BRIEF): Any =
    mapOf(
        "account_id" to id,
        "username" to username,
        "cabinet" to pads.toDetailVO(level)
    )

fun CoIndividual.toChangePasswordVO(): Any =
    mapOf(
        "account_id" to id,
        "username" to username
    )

fun CoIndividual.toResetPasswordVO(): Any =
    toChangePasswordVO()

fun CoIndividual.toInfoChangeVO(): Any =
    toChangePasswordVO()

fun Set<CoPadControlBlock>.toDetailVO(level: DetailLevel = DetailLevel.BRIEF): Any {
    val padsInfo = when (level) {
        DetailLevel.BRIEF -> map {
            mapOf(
                "title" to it.pad.title,
                "owner" to it.individual.username,
                "share_level" to it.status
            )
        }

        DetailLevel.DETAIL -> map {
            mapOf(
                "share_level" to it.status,
                "pad" to it.pad.toDetailVO(),
                "owner" to it.individual.username
            )
        }
    }

    return mapOf(
        "pad_count" to size,
        "pads" to padsInfo
    )
}

fun CoPad.toCreateVO(): Any = mapOf(
    "pad_id" to id,
    "title" to title,
    "owner" to belongTo.username,
    "created_time" to createdTime
)

fun CoPad.toDetailVO(): Any = mapOf(
    "pad_id" to id,
    "title" to title,
    "body" to body,
    "owner" to belongTo.username,
    "created_time" to createdTime,
    "instants" to instants.map { id },
    "is_locked" to isLocked,
    "workers" to workers.map {
        mapOf(
            "account_id" to it.individual.id,
            "username" to it.individual.username,
            "share_level" to it.status,
            "share_time" to createdTime
        )
    }
)

private fun CoBlame.toDetailVO(): Any = mapOf(
    "account_id" to belongTo.id,
    "username" to belongTo.username,
    "range" to "$left-$right",
    "created_time" to createdTime
)

fun CoPadInstant.toRevisionDetailVO(): Any = mapOf(
    "revision_id" to id,
    "body" to body,
    "created_time" to createdTime,
    "tag" to tag,
    "created_by" to createdBy,
    "contributes" to contributes.map { it.toDetailVO() }
)

fun CoPadInstant.toInstantSavedVO(): Any = mapOf(
    "instant_id" to id,
    "pad_id" to belongTo.id,
    "title" to belongTo.title,
    "body" to body,
    "tag" to tag,
    "created_by" to createdBy.username,
    "created_time" to createdTime,
    "contributes" to contributes.map { it.toDetailVO() }
)

fun CoInviteLink.toCreateVO(): Any = mapOf(
    "token" to token,
    "pad" to pad.id,
    "invitee" to invitee,
    "permission" to permission,
    "created_time" to createdTime,
    "expired_time" to expiredTime
)

fun CoPad.socketPadFetchVO(): Any {
    return object {}
}

fun CoPad.socketLockReleasedVO(): Any {
    return object {}
}

fun CoLock.socketLockAcquireVO(): Any {
    return object {}
}

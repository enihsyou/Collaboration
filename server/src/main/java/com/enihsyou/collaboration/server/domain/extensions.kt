package com.enihsyou.collaboration.server.domain

import com.enihsyou.collaboration.server.util.DetailLevel

fun CoIndividual.toCreateVO(): Any {
    return object {}
}

fun CoIndividual.toLoginVO(): Any {
    return object {}
}

fun CoIndividual.toDetailVO(level: DetailLevel = DetailLevel.BRIEF): Any {
    return object {}
}

fun CoIndividual.toChangePasswordVO(): Any {
    return object {}
}

fun CoIndividual.toResetPasswordVO(): Any {
    return object {}
}

fun CoIndividual.toInfoChangeVO(): Any {
    return object {}
}

fun CoCabinet.toDetailVO(level: DetailLevel = DetailLevel.BRIEF): Any {
    return object {}
}

fun CoPad.toCreateVO(): Any {
    return object {}
}

fun CoPad.toDetailVO(): Any {
    return object {}
}

fun CoPad.toRevisionDetailVO(): Any {
    return object {}
}

fun CoPadInstant.toInstantSavedVO(): Any {
    return object {}
}

fun CoInviteLink.toCreateVO(): Any {
    return object {}
}

fun CoPad.getTitleImpl(): String {
    return instants.sortedBy { it.createdTime }.lastOrNull()?.title ?: ""
}

fun CoPad.socketPadFetchVO(): Any {
    return object {}
}

fun CoPad.socketLockReleasedVO(): Any {
    return object {}
}

fun CoLock.socketLockAcquireVO(): Any {
    return object {}
}

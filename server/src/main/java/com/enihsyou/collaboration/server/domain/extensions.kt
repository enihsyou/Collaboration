package com.enihsyou.collaboration.server.domain

import com.enihsyou.collaboration.server.util.DetailLevel

fun CoIndividual.toCreateVO(): Any {
    return object {}
}

fun CoIndividual.toLoginVO(): Any {
    return object {}
}

fun CoIndividual.toInfoVO(level: String = DetailLevel.LEVEL_BRIEF): Any {
    return object {}
}

fun CoIndividual.toChangePasswordVO(): Any {
    return object {}
}

fun CoIndividual.toInfoChangeVO(): Any {
    return object {}
}

internal fun CoPad.getTitleImpl(): String {
    return instants.sortedBy { it.createdTime }.lastOrNull()?.title ?: ""
}

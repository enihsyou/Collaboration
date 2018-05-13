package com.enihsyou.collaboration.server.util

import com.enihsyou.collaboration.server.domain.CoIndividual
import com.enihsyou.collaboration.server.pojo.NeedLoginException
import org.springframework.security.core.context.SecurityContextHolder

object PermissionUtils {

    @JvmStatic
    fun currentAccount(): CoIndividual? = SecurityContextHolder.getContext().authentication?.details as? CoIndividual

    @JvmStatic
    fun loggedAccount(): CoIndividual = currentAccount() ?: throw NeedLoginException()
}

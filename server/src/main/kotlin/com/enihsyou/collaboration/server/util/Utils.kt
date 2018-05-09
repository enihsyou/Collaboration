package com.enihsyou.collaboration.server.util

import com.enihsyou.collaboration.server.domain.CoIndividual
import com.enihsyou.collaboration.server.pojo.NeedLoginException
import org.springframework.security.core.context.SecurityContextHolder

object PermissionUtils {

    fun currentAccount(): CoIndividual? = SecurityContextHolder.getContext().authentication?.details as? CoIndividual
    fun loggedAccount(): CoIndividual = currentAccount() ?: throw NeedLoginException()
}

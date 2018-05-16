package com.enihsyou.collaboration.server.util

import com.enihsyou.collaboration.server.domain.CoIndividual
import com.enihsyou.collaboration.server.pojo.NeedLoginException
import com.enihsyou.collaboration.server.repository.CabinetRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
object PermissionUtils {

    private fun authentication(): Authentication? = SecurityContextHolder.getContext().authentication

    private fun currentAccount(): CoIndividual? = authentication()?.details as? CoIndividual

    @Autowired
    lateinit var cabinetRepository: CabinetRepository // fixme

    @JvmStatic
    fun loggedAccount(): CoIndividual = currentAccount() ?: throw NeedLoginException()

    @JvmStatic
    fun currentUsername(): String? = authentication()?.name

    /**检查用户[username]是否拥有文稿[padId]*/
    @JvmStatic
    fun checkOwnership(padId: Long, username: String?) {
        username ?: throw NeedLoginException()
    }

    /**检查用户[username]是否能编辑文稿[padId]*/
    @JvmStatic
    fun checkCoopship(padId: Long, username: String?) {
        username ?: throw NeedLoginException()
        cabinetRepository.isByIdBelongToUsername(padId, username)
    }
}

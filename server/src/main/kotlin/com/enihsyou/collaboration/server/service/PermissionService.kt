package com.enihsyou.collaboration.server.service

import com.enihsyou.collaboration.server.domain.CoIndividual
import com.enihsyou.collaboration.server.pojo.NeedLoginException
import com.enihsyou.collaboration.server.repository.IndividualRepository
import com.enihsyou.collaboration.server.service.PermissionUtil.currentUsername
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class PermissionService(
    private val individualRepository: IndividualRepository
) {

    /**获取当前登陆的用户对象*/
    fun loggedAccount(): CoIndividual =
        currentUsername()?.let { individualRepository.findByUsername(it) }
            ?: throw NeedLoginException()

    /**检查用户[username]是否拥有文稿[padId]*/
    fun checkOwnership(padId: Long, username: String?) {
        username ?: throw NeedLoginException()
        // cabinetRepository.isPadBelongsToUser(padId, username)
        // todo
    }

    /**检查用户[username]是否能编辑文稿[padId]*/
    fun checkCoopship(padId: Long, username: String?) {
        username ?: throw NeedLoginException()

        // todo
    }
}

object PermissionUtil {
    @JvmStatic
    private fun authentication(): Authentication? = SecurityContextHolder.getContext().authentication

    @JvmStatic
    fun currentUsername(): String? = authentication()?.name
}

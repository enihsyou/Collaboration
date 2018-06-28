package com.enihsyou.collaboration.server.service

import com.enihsyou.collaboration.server.domain.CoIndividual
import com.enihsyou.collaboration.server.pojo.NeedLoginException
import com.enihsyou.collaboration.server.pojo.PadNotOwnedException
import com.enihsyou.collaboration.server.pojo.UserNotExistException
import com.enihsyou.collaboration.server.repository.IndividualRepository
import com.enihsyou.collaboration.server.service.PermissionUtil.currentUsername
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

/**处理权限相关的业务逻辑*/
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

        val account = fetchAccount(username)
        if (account.pads.filter { it.pad.id == padId }
                .none { it.pad.belongTo.username == username })
            throw PadNotOwnedException(padId, username)

    }

    /**检查用户[username]是否能编辑文稿[padId]*/
    fun checkCoopship(padId: Long, username: String?) {
        username ?: throw NeedLoginException()

        val account = fetchAccount(username)
        if (account.pads.none { it.pad.id == padId })
            throw PadNotOwnedException(padId, username)
    }

    /**从数据库中获取用户名为[username]的记录*/
    private fun fetchAccount(username: String) =
        individualRepository.findByUsername(username) ?: throw UserNotExistException(username)
}

/**处理权限相关的业务逻辑*/
object PermissionUtil {

    @JvmStatic
    private fun authentication(): Authentication? = SecurityContextHolder.getContext().authentication

    @JvmStatic
    fun currentUsername(): String? = authentication()?.name
}

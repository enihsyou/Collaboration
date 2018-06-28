package com.enihsyou.collaboration.server.service.impl

import com.enihsyou.collaboration.server.domain.CoIndividual
import com.enihsyou.collaboration.server.domain.CoInviteLink
import com.enihsyou.collaboration.server.domain.CoLinkStatus
import com.enihsyou.collaboration.server.domain.CoPad
import com.enihsyou.collaboration.server.domain.CoPadControlBlock
import com.enihsyou.collaboration.server.domain.CoPadInstant
import com.enihsyou.collaboration.server.pojo.InstantNotExistException
import com.enihsyou.collaboration.server.pojo.InviteLinkHasExpiredException
import com.enihsyou.collaboration.server.pojo.PadCreateDTO
import com.enihsyou.collaboration.server.pojo.PadSaveDTO
import com.enihsyou.collaboration.server.pojo.PadUpdateDTO
import com.enihsyou.collaboration.server.repository.IndividualRepository
import com.enihsyou.collaboration.server.repository.InviteLinkRepository
import com.enihsyou.collaboration.server.repository.PadBlockRepository
import com.enihsyou.collaboration.server.repository.PadRepository
import com.enihsyou.collaboration.server.service.PadService
import com.enihsyou.collaboration.server.util.ShareLevel
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*

@Service
class PadServiceImpl(
    private val padRepository: PadRepository,
    private val padBlockRepository: PadBlockRepository,
    private val individualRepository: IndividualRepository,
    private val inviteLinkRepository: InviteLinkRepository
) : PadService {

    override fun listPads(account: CoIndividual): MutableSet<CoPadControlBlock> {
        return account.pads
    }

    override fun fetchPad(padId: Long): CoPad {
        return padRepository.queryById(padId)
    }

    override fun createPad(padCreateDTO: PadCreateDTO, account: CoIndividual): CoPad {
        val (title) = padCreateDTO

        /*创建文稿数据实体*/
        val pad = CoPad()
            .setBelongTo(account)
            .setTitle(title)


        /*保存新创建的文稿到数据库*/
        padRepository.save(pad)

        val link = CoInviteLink()
            .setPad(pad)
            .setPermission(CoLinkStatus.CAN_VIEW)
            .setToken(UUID.randomUUID().toString())

        pad.inviteLink = link

        /*创建多对多关系*/
        val block = CoPadControlBlock(account, pad)
            .setStatus(CoLinkStatus.OWN)

        pad.workers += block

        return pad
    }

    override fun updatePad(padId: Long, padUpdateDTO: PadUpdateDTO): CoPad {
        val (title) = padUpdateDTO

        /*从数据库中获取对象*/
        val pad = fetchPad(padId)

        /*修改为新的标题*/
        pad.title = title

        return pad
    }

    override fun deletePad(padId: Long) {
        padRepository.deleteById(padId)
    }

    override fun fetchRevision(padId: Long, revisionId: Long): CoPadInstant {
        /*从数据库中获取对象*/
        val pad = fetchPad(padId)

        return pad.instants.firstOrNull { it.id == revisionId }
            ?: throw InstantNotExistException(padId, revisionId)
    }

    override fun addTagToInstant(
        padId: Long,
        revisionId: Long,
        padSaveDTO: PadSaveDTO
    ): CoPadInstant {
        val (tag) = padSaveDTO

        /*从数据库中获取对象*/
        val pad = fetchPad(padId)

        val instant = pad.instants.firstOrNull { it.revision == revisionId }
            ?: throw InstantNotExistException(padId, revisionId)

        instant.tag = tag

        return instant
    }

    override fun revertInstant(padId: Long, revisionId: Long): CoPad {
        /*从数据库中获取对象*/
        val pad = fetchPad(padId)
        val revert = pad.instants.firstOrNull { it.revision == revisionId }
            ?: throw InstantNotExistException(padId, revisionId)

        /*重设数据*/
        pad.apply {
            body = revert.body
            contributes.retainAll(revert.contributes)
            contributes.addAll(revert.contributes)
            /*删除之后的历史记录*/
            instants.removeIf { it.createdTime > revert.createdTime }
            locks.clear()
        }

        padRepository.save(pad)

        return pad
    }

    override fun reassignPadShareLevel(padId: Long, shareLevel: ShareLevel): CoInviteLink {
        /*从数据库中获取对象*/
        val pad = fetchPad(padId)

        /*修改邀请链接权限 生成新token*/
        val link = pad.inviteLink
            .setPermission(shareLevel.toCoLinkStatus())
            .setToken(UUID.randomUUID().toString())

        inviteLinkRepository.save(link)

        return link
    }

    override fun reassignPadShareLevelToMember(padId: Long, shareLevel: ShareLevel, invitee: String): CoPad {
        /*从数据库中获取对象*/
        val pad = fetchPad(padId)
        val inviteeAccount = individualRepository.getUser(invitee)


        val link = CoPadControlBlock(inviteeAccount, pad)
            .setStatus(shareLevel.toCoLinkStatus())

        padBlockRepository.save(link)

        padRepository.save(pad)

        return pad
    }

    override fun removePadShareMember(padId: Long, invitee: String): CoPad {
        /*从数据库中获取对象*/
        val pad = fetchPad(padId)

        pad.workers.removeIf {
            it.individual.username == invitee
        }
        pad.locks.removeIf {
            it.locker.username == invitee
        }
        /*其他数据先不移除*/

        return pad
    }

    override fun revokeShare(padId: Long, target: String) {
        /*从数据库中获取对象*/
        val pad = fetchPad(padId)

        pad.workers.removeIf { it.individual.username == target }
    }

    override fun joinPad(token: String, account: CoIndividual): CoPad {

        /*从数据库中获取对象*/
        val link = inviteLinkRepository.queryByToken(token)

        if (link.isExpired) throw InviteLinkHasExpiredException(token)

        val block = CoPadControlBlock(account, link.pad)
            .setStatus(link.permission)

        link.pad.workers += block

        println(link.pad.workers)

        return link.pad
    }

    companion object {

        private val LOGGER = LoggerFactory.getLogger(PadService::class.java)
    }
}

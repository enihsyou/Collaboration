package com.enihsyou.collaboration.server.service.impl

import com.enihsyou.collaboration.server.domain.CoIndividual
import com.enihsyou.collaboration.server.domain.CoLock
import com.enihsyou.collaboration.server.domain.CoPad
import com.enihsyou.collaboration.server.pojo.FetchPadDTO
import com.enihsyou.collaboration.server.pojo.LockAcquireDTO
import com.enihsyou.collaboration.server.pojo.LockReleaseDTO
import com.enihsyou.collaboration.server.pojo.WrongArgumentException
import com.enihsyou.collaboration.server.repository.LockRepository
import com.enihsyou.collaboration.server.service.DiffService
import com.enihsyou.collaboration.server.service.DocumentService
import com.enihsyou.collaboration.server.service.PadService
import com.enihsyou.collaboration.server.service.WebsocketService
import org.slf4j.LoggerFactory
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.stereotype.Service

@Service
class WebsocketServiceImpl(
    private val template: SimpMessageSendingOperations,
    private val diffService: DiffService,
    private val padService: PadService,
    private val documentService: DocumentService,
    private val lockRepository: LockRepository
) : WebsocketService {

    override fun fetchStatus(fetchPadDTO: FetchPadDTO): CoPad {
        val (pad_id, client_revision) = fetchPadDTO

        val pad = padService.fetchPad(pad_id)

        return pad
    }

    override fun acquireLock(lockAcquireDTO: LockAcquireDTO, account: CoIndividual): CoLock {
        val (pad_id, client_revision, lock_range) = lockAcquireDTO
        val pad = padService.fetchPad(pad_id)

//        if (client_revision <= pad.)

        val lock = documentService.acquire(pad, lock_range, account)
        return lock
    }

    override fun releaseLock(lockReleaseDTO: LockReleaseDTO, account: CoIndividual): CoPad {
        val (pad_id, client_revision, lock_id, modified, replace_with) = lockReleaseDTO

        val pad = padService.fetchPad(pad_id)
        if (!modified || replace_with == null) return pad

        val lock = lockRepository.queryById(lock_id)

        documentService.release(lock, replace_with)

        return pad
    }

    private fun String.toRange(): IntRange {
        val split = split("-")
            .takeIf { it.size == 2 } ?: throw WrongArgumentException(this)

        val (first, second) = split
            .map { it.toIntOrNull() ?: throw WrongArgumentException(it, adition = "不是整数") }

        return first..second
    }

    companion object {

        private val LOGGER = LoggerFactory.getLogger(WebsocketService::class.java)
    }
}

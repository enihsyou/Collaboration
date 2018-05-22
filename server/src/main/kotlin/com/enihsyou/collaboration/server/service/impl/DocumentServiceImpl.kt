package com.enihsyou.collaboration.server.service.impl

import com.enihsyou.collaboration.server.domain.CoIndividual
import com.enihsyou.collaboration.server.domain.CoLock
import com.enihsyou.collaboration.server.domain.CoPad
import com.enihsyou.collaboration.server.service.DocumentService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class DocumentServiceImpl : DocumentService {

    override fun lock(pad: CoPad, range: IntRange, operator: CoIndividual): CoLock {
        TODO("not implemented") // todo
    }

    override fun release(lock: CoLock, contributor: CoIndividual): CoPad {
        TODO("not implemented") // todo
    }

    companion object {

        private val LOGGER = LoggerFactory.getLogger(DocumentService::class.java)
    }
}

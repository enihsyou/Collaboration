package com.enihsyou.collaboration.server.service.impl

import com.enihsyou.collaboration.server.domain.CoIndividual
import com.enihsyou.collaboration.server.domain.CoLock
import com.enihsyou.collaboration.server.domain.CoPad
import com.enihsyou.collaboration.server.pojo.RangeCollapsedException
import com.enihsyou.collaboration.server.service.DocumentService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import kotlin.math.max
import kotlin.math.min

@Service
class DocumentServiceImpl : DocumentService {

    override fun lock(pad: CoPad, range: IntRange, operator: CoIndividual): CoLock {
        pad.locks.forEach {
            if (max(it.left, range.first) < min(it.right, range.last))
                throw RangeCollapsedException(range, it.range)
        }

        val lock = CoLock()
            .setBelongTo(operator)
            .setRagne(range)
    }

    override fun release(lock: CoLock): CoPad {
        TODO("not implemented") // todo
    }

    companion object {

        private val LOGGER = LoggerFactory.getLogger(DocumentService::class.java)
    }
}

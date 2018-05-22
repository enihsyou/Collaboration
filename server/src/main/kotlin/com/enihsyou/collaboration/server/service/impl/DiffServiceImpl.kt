package com.enihsyou.collaboration.server.service.impl

import com.enihsyou.collaboration.server.pojo.DiffResult
import com.enihsyou.collaboration.server.service.DiffService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class DiffServiceImpl : DiffService {

    override fun compare(former: String, latter: String, diff: IntRange): DiffResult {


        return DiffResult("")
    }

    companion object {

        private val LOGGER = LoggerFactory.getLogger(DiffService::class.java)
    }
}

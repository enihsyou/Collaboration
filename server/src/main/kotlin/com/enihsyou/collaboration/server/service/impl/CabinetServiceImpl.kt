package com.enihsyou.collaboration.server.service.impl

import com.enihsyou.collaboration.server.domain.CoCabinet
import com.enihsyou.collaboration.server.domain.CoIndividual
import com.enihsyou.collaboration.server.domain.CoPad
import com.enihsyou.collaboration.server.pojo.PadCreateDTO
import com.enihsyou.collaboration.server.pojo.PadUpdateDTO
import com.enihsyou.collaboration.server.repository.CabinetRepository
import com.enihsyou.collaboration.server.repository.PadRepository
import com.enihsyou.collaboration.server.service.CabinetService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class CabinetServiceImpl(
    private val cabinetRepository: CabinetRepository,
    private val padRepository: PadRepository
) : CabinetService {

    override fun fetchCabinet(account: CoIndividual): CoCabinet {
        return account.cabinet
    }

    override fun createPad(padCreateDTO: PadCreateDTO, account: CoIndividual): CoPad {
        val (title) = padCreateDTO

        /*创建数据实体*/
        val pad = CoPad()
            .setBelongTo(account.cabinet)
            .setTitle(title)

        /*保存到数据库*/
        padRepository.save(pad)

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

    override fun fetchPad(padId: Long): CoPad {
        return padRepository.queryById(padId)
    }

    companion object {

        private val LOGGER = LoggerFactory.getLogger(CabinetService::class.java)
    }
}

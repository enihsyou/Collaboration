package com.enihsyou.collaboration.server.service

import com.enihsyou.collaboration.server.domain.CoCabinet
import com.enihsyou.collaboration.server.domain.CoIndividual
import com.enihsyou.collaboration.server.domain.CoPad
import com.enihsyou.collaboration.server.pojo.PadCreateDTO
import com.enihsyou.collaboration.server.pojo.PadUpdateDTO

/** 处理用户文件柜相关的逻辑  */
interface CabinetService {

    /**
     * 获得用户的[文件柜][CoCabinet]对象
     *
     * @param account 获取的账户
     */
    fun fetchCabinet(account: CoIndividual): CoCabinet

    /**
     * 在用户的文件柜里创建一篇属于自己的新的[文稿][CoPad]
     *
     * @param account 获取的账户
     */
    fun createPad(padCreateDTO: PadCreateDTO, account: CoIndividual): CoPad

    /**
     * 更新文稿信息，比如标题什么的
     *
     * @param padId 更新目标
     */
    fun updatePad(padId: Long, padUpdateDTO: PadUpdateDTO): CoPad

    /**
     * 获取一篇文档
     *
     * @param padId 获取目标
     */
    fun fetchPad(padId: Long): CoPad
}

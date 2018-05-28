package com.enihsyou.collaboration.server.service

import com.enihsyou.collaboration.server.domain.CoIndividual
import com.enihsyou.collaboration.server.domain.CoLock
import com.enihsyou.collaboration.server.domain.CoPad
import com.enihsyou.collaboration.server.pojo.FetchPadDTO
import com.enihsyou.collaboration.server.pojo.LockAcquireDTO
import com.enihsyou.collaboration.server.pojo.LockReleaseDTO

/** 处理Websocket相关的逻辑  */
interface WebsocketService {

    /**获取编辑中文稿的最新状态 */
    fun fetchStatus(fetchPadDTO: FetchPadDTO): CoPad

    /**用户获取文稿的编辑🔒 */
    fun acquireLock(lockAcquireDTO: LockAcquireDTO, account: CoIndividual): CoLock

    /** 用户释放文稿的编辑🔒 */
    fun releaseLock(lockReleaseDTO: LockReleaseDTO, account: CoIndividual): CoPad
}

package com.enihsyou.collaboration.server.service

import com.enihsyou.collaboration.server.domain.CoIndividual
import com.enihsyou.collaboration.server.domain.CoLock
import com.enihsyou.collaboration.server.domain.CoPad

/** 处理多用户对文稿进行同步更新，以及🔒控制相关的逻辑  */
interface DocumentService { // todo use Command Pattern https://refactoring.guru/design-patterns/command

    /** 锁定指定范围
     * @param pad 锁定目标文档
     * @param range 锁定范围，左闭右开区间
     * @param operator 执行锁定的用户
     * */
    fun lock(pad: CoPad, range: IntRange, operator: CoIndividual): CoLock

    /** 释放一个锁 */
    fun release(lock: CoLock, contributor: CoIndividual): CoPad
}

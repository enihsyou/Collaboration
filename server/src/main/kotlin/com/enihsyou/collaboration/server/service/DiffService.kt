package com.enihsyou.collaboration.server.service

import com.enihsyou.collaboration.server.pojo.DiffResult

/** 处理更新对比相关的逻辑  */
interface DiffService {

    /**对比[former]和[latter]*/
    fun compare(former: String, latter: String, diff: IntRange): DiffResult
}

package com.enihsyou.collaboration.server.service

import com.enihsyou.collaboration.server.pojo.DiffResult

/** 处理更新对比相关的逻辑  */
interface DiffService {

    fun compare(): DiffResult
}

package com.enihsyou.collaboration.server.service;

import com.enihsyou.collaboration.server.domain.CoIndividual;
import com.enihsyou.collaboration.server.domain.CoLock;
import com.enihsyou.collaboration.server.domain.CoPad;
import com.enihsyou.collaboration.server.pojo.FetchPadDTO;
import com.enihsyou.collaboration.server.pojo.LockAcquireDTO;
import com.enihsyou.collaboration.server.pojo.LockReleaseDTO;

/** 处理Websocket相关的逻辑 */
public interface WebsocketService {

    /**获取编辑中文稿的最新状态*/
    CoPad fetchStatus(FetchPadDTO fetchPadDTO);

    /**用户获取文稿的编辑🔒*/
    CoLock acquireLock(LockAcquireDTO lockAcquireDTO, CoIndividual account);

    /** 用户释放文稿的编辑🔒 */
    CoPad releaseLock(LockReleaseDTO lockReleaseDTO, CoIndividual account);
}

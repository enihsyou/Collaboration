package com.enihsyou.collaboration.server.service;

import com.enihsyou.collaboration.server.domain.CoCabinet;
import com.enihsyou.collaboration.server.domain.CoIndividual;
import com.enihsyou.collaboration.server.domain.CoPad;
import com.enihsyou.collaboration.server.pojo.PadCreateDTO;

/** 处理用户文件柜相关的逻辑 */
public interface CabinetService {

    /**
     * 获得用户的{@link CoCabinet 文件柜}对象
     *
     * @param account
     */
    CoCabinet fetchCabinet(final CoIndividual account);

    /** 在用户的文件柜里创建一篇属于自己的新的{@link CoPad 文稿} */
    CoPad createPad(PadCreateDTO padCreateDTO, final CoIndividual account);

    /** 获取用户的一篇文档 */
    CoPad fetchPad(long padId, CoIndividual account);
}

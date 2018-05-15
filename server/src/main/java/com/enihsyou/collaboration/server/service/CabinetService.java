package com.enihsyou.collaboration.server.service;

import com.enihsyou.collaboration.server.domain.CoCabinet;
import com.enihsyou.collaboration.server.domain.CoIndividual;
import com.enihsyou.collaboration.server.domain.CoPad;
import com.enihsyou.collaboration.server.pojo.PadCreateDTO;
import com.enihsyou.collaboration.server.pojo.PadUpdateDTO;

/** 处理用户文件柜相关的逻辑 */
public interface CabinetService {

    /**
     * 获得用户的{@link CoCabinet 文件柜}对象
     *
     * @param account 获取的账户
     */
    CoCabinet fetchCabinet(final CoIndividual account);

    /**
     * 在用户的文件柜里创建一篇属于自己的新的{@link CoPad 文稿}
     *
     * @param account 获取的账户
     */
    CoPad createPad(final PadCreateDTO padCreateDTO, final CoIndividual account);

    /**
     * 更新文稿信息，比如标题什么的
     *
     * @param padId 更新目标
     */
    CoPad updatePad(final long padId, PadUpdateDTO padUpdateDTO);

    /**
     * 获取一篇文档
     *
     * @param padId 获取目标
     */
    CoPad fetchPad(final long padId);
}

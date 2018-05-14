package com.enihsyou.collaboration.server.service;

import com.enihsyou.collaboration.server.domain.CoIndividual;
import com.enihsyou.collaboration.server.domain.CoInviteLink;
import com.enihsyou.collaboration.server.domain.CoPad;
import com.enihsyou.collaboration.server.domain.CoPadInstant;
import com.enihsyou.collaboration.server.pojo.PadSaveDTO;
import com.enihsyou.collaboration.server.util.ShareLevel;

/** 处理文稿相关的逻辑 */
public interface PadService {

    /** 保存文稿的当前状态，创建一个历史记录 */
    CoPadInstant saveInstant(long padId, PadSaveDTO padSaveDTO, CoIndividual account);

    /** 将文稿回滚到之前的一个历史记录状态 */
    CoPad revertInstant(long padId, long revision, CoIndividual account);

    /** 以指定的暴露等级分享文档，创建分享链接 */
    CoInviteLink sharePad(long padId, ShareLevel shareLevel, CoIndividual account);

    /** 使用令牌加入协同文档的编辑 */
    CoPad joinPad(String token, CoIndividual account);
}

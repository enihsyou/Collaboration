package com.enihsyou.collaboration.server.service

import com.enihsyou.collaboration.server.domain.CoIndividual
import com.enihsyou.collaboration.server.domain.CoInviteLink
import com.enihsyou.collaboration.server.domain.CoPad
import com.enihsyou.collaboration.server.domain.CoPadInstant
import com.enihsyou.collaboration.server.pojo.PadSaveDTO
import com.enihsyou.collaboration.server.util.ShareLevel

/** 处理文稿相关的逻辑  */
interface PadService {

    /**
     * 保存文稿的当前状态，创建一个历史记录
     *
     * @param padId      目标文稿
     * @param padSaveDTO 传来的所需参数
     */
    fun saveInstant(padId: Long, padSaveDTO: PadSaveDTO): CoPadInstant

    /**
     * 将文稿回滚到之前的一个历史记录状态
     * * @param padId    目标文稿
     *
     * @param revision 目标版本号
     */
    fun revertInstant(padId: Long, revision: Long): CoPad

    /**
     * 以指定的暴露等级分享文档，创建分享链接
     *
     * @param padId      创建分享链接的目标文稿
     * @param shareLevel 期望的分享等级
     */
    fun sharePad(padId: Long, shareLevel: ShareLevel): CoInviteLink

    /**
     * 已经登录的用户使用令牌加入协同文档的编辑
     *
     * @param token   提供的令牌
     * @param account 正在进行加入的账户
     */
    fun joinPad(token: String, account: CoIndividual): CoPad
}

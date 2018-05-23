package com.enihsyou.collaboration.server.service

import com.enihsyou.collaboration.server.domain.CoIndividual
import com.enihsyou.collaboration.server.domain.CoInviteLink
import com.enihsyou.collaboration.server.domain.CoPad
import com.enihsyou.collaboration.server.domain.CoPadControlBlock
import com.enihsyou.collaboration.server.domain.CoPadInstant
import com.enihsyou.collaboration.server.pojo.PadCreateDTO
import com.enihsyou.collaboration.server.pojo.PadSaveDTO
import com.enihsyou.collaboration.server.pojo.PadUpdateDTO
import com.enihsyou.collaboration.server.util.ShareLevel

/** 处理文稿相关的逻辑  */
interface PadService {

    /**
     * 获得用户的所有文稿
     *
     * 直接返回数据库查询结果
     *
     * @param account 获取的账户
     */
    fun listPads(account: CoIndividual): MutableSet<CoPadControlBlock>

    /**
     * 获取一篇文档
     *
     * 直接返回数据库关联结果
     *
     * @param padId 获取目标
     */
    fun fetchPad(padId: Long): CoPad

    /**
     * 在用户的文件柜里创建一篇属于自己的新的[文稿][CoPad]
     *
     * 首先创建保存实体，设置好拥有者和标题
     * 保存到数据库中后设置和用户的多对多关系
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
     * 在用户的文件柜里删除一篇文稿
     *
     * @param padId 删除目标
     */
    fun deletePad(padId: Long)

    /**
     * 获取文稿的一个历史版本
     *
     * @param padId 获取目标文稿
     * @param revisionId 获取目标版本号
     */
    fun fetchRevision(padId: Long, revisionId: String): CoPadInstant

    /**
     * 用户为历史记录添加标记
     *
     * @param padId      目标文稿id
     * @param revisionId 需要添加标记的版本号
     * @param padSaveDTO 传来的所需参数
     */
    fun addTagToInstant(
        padId: Long,
        revisionId: String,
        padSaveDTO: PadSaveDTO
    ): CoPadInstant

    /**
     * 将文稿回滚到之前的一个历史记录状态
     *
     * 重设数据以后，回滚版本之后的历史版本，将被删除
     *
     * @param padId    目标文稿
     * @param revisionId 目标版本号
     */
    fun revertInstant(padId: Long, revisionId: String): CoPad

    /**
     * 以指定的暴露等级分享文档，创建分享链接
     *
     * @param padId      创建分享链接的目标文稿
     * @param shareLevel 期望的分享等级
     */
    fun sharePad(padId: Long, shareLevel: ShareLevel): CoInviteLink

    /**
     * 以指定的暴露等级分享文档给指定用户，创建分享链接
     *
     * @param padId      创建分享链接的目标文稿
     * @param shareLevel 期望的分享等级
     * @param invitee 分享给的用户名
     */
    fun sharePadTo(padId: Long, shareLevel: ShareLevel, invitee: String): CoInviteLink

    /**
     * 移除文稿的分享令牌
     *
     * @param padId      分享的目标文稿
     * @param target 期望移除的对象用户名
     */
    fun revokeShare(padId: Long, target: String)

    /**
     * 已经登录的用户使用令牌加入协同文档的编辑
     *
     * @param token   提供的令牌
     * @param account 正在进行加入的账户
     */
    fun joinPad(token: String, account: CoIndividual): CoPad
}

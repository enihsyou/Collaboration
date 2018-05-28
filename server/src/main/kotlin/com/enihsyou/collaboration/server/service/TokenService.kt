package com.enihsyou.collaboration.server.service

import com.enihsyou.collaboration.server.domain.CoInviteLink

/** 处理授权令牌相关的逻辑  */
interface TokenService {

    /** 创建一个分享文稿的令牌，并持久化 */
    fun generateForPadSharing(padId: Long): CoInviteLink

    /** 修改分享文稿的令牌的相关信息 */
    fun modifyPadSharingToken(padId: Long): CoInviteLink

    /** 验证并使用令牌加入文稿合作编辑 */
    fun useToJoinPad(token: String, account: CoInviteLink)

    /** 生成重置密码的令牌，并持久化 */
    fun generateForPasswordReset(username: String): String
}

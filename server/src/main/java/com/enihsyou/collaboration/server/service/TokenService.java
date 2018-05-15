package com.enihsyou.collaboration.server.service;

import com.enihsyou.collaboration.server.domain.CoInviteLink;

/** 处理授权令牌相关的逻辑 */
public interface TokenService {

    /** 创建一个分享文稿的令牌，并持久化 */
    CoInviteLink generateForPadSharing(final long padId);

    /** 修改分享文稿的令牌的相关信息 */
    CoInviteLink modifyPadSharingToken(final long padId);

    /** 验证并使用令牌加入文稿合作编辑 */
    void useToJoinPad(String token, CoInviteLink account);
}

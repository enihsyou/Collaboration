package com.enihsyou.collaboration.server.domain

/**邀请链接的等级权限*/
enum class CoLinkStatus {
    /**用户拥有这篇文稿*/
    OWN,

    /**用户能查看文稿*/
    CAN_VIEW,

    /**用户能参与文稿的编辑*/
    CAN_EDIT,

    /**授予用户的权限被收回*/
    REVOKED
}

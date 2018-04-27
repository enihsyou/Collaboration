package com.enihsyou.collaboration.server.domain

enum class CoLinkStatus {

    /**用户能查看文稿*/
    CAN_VIEW,

    /**用户能参与文稿的编辑*/
    CAN_EDIT,

    /**授予用户的权限被收回*/
    REVOKED
}

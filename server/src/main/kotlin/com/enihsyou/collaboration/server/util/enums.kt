package com.enihsyou.collaboration.server.util

import com.enihsyou.collaboration.server.domain.CoLinkStatus
import com.enihsyou.collaboration.server.pojo.WrongArgumentException

/**详细等级*/
enum class DetailLevel(
    val string: String
) {

    /**简要粒度*/
    BRIEF(DetailLevel.LEVEL_BRIEF),

    /**详细粒度*/
    DETAIL(DetailLevel.LEVEL_DETAIL);

    companion object {
        const val LEVEL_BRIEF = "brief"
        const val LEVEL_DETAIL = "detail"

        @JvmStatic
        fun parseLevel(string: String): DetailLevel {
            return when (string.toLowerCase()) {
                LEVEL_BRIEF  -> BRIEF
                LEVEL_DETAIL -> DETAIL
                else         -> throw WrongArgumentException(string)
            }
        }
    }
}

/**分享等级*/
enum class ShareLevel(
    val string: String
) {

    /**用户能查看文稿*/
    CAN_VIEW(ShareLevel.LEVEL_VIEW),

    /**用户能参与文稿的编辑*/
    CAN_EDIT(ShareLevel.LEVEL_EDIT);

    fun toCoLinkStatus(): CoLinkStatus {
        return when (this) {
            ShareLevel.CAN_VIEW -> CoLinkStatus.CAN_VIEW
            ShareLevel.CAN_EDIT -> CoLinkStatus.CAN_EDIT
        }
    }

    companion object {
        const val LEVEL_VIEW = "view"
        const val LEVEL_EDIT = "edit"

        @JvmStatic
        fun parseLevel(string: String): ShareLevel {
            return when (string.toLowerCase()) {
                LEVEL_VIEW -> CAN_VIEW
                LEVEL_EDIT -> CAN_EDIT
                else       -> throw WrongArgumentException(string)
            }
        }
    }
}


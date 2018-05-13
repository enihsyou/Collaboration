package com.enihsyou.collaboration.server.util

import com.enihsyou.collaboration.server.pojo.WrongArgumentException

enum class DetailLevel(
    val string: String
) {

    BRIEF(DetailLevel.LEVEL_BRIEF),
    DETAIL(DetailLevel.LEVEL_DETAIL);

    companion object {
        const val LEVEL_BRIEF = "brief"
        const val LEVEL_DETAIL = "detail"

        @JvmStatic
        fun parseLevel(string: String): DetailLevel {
            return try {
                DetailLevel.valueOf(string)
            } catch (e: IllegalArgumentException) {
                throw WrongArgumentException(string)
            }
        }
    }
}

enum class ShareLevel(
    val string: String
) {

    /**用户能查看文稿*/
    CAN_VIEW(ShareLevel.LEVEL_VIEW),

    /**用户能参与文稿的编辑*/
    CAN_EDIT(ShareLevel.LEVEL_EDIT);

    companion object {
        const val LEVEL_VIEW = "view"
        const val LEVEL_EDIT = "edit"

        @JvmStatic
        fun parseLevel(string: String): ShareLevel {
            return try {
                ShareLevel.valueOf(string)
            } catch (e: IllegalArgumentException) {
                throw WrongArgumentException(string)
            }
        }
    }
}


package com.enihsyou.collaboration.server.util

import java.time.ZoneId

const val URL_BASE = "/api/"
const val TABLE_PREFIX = "co_"
const val URL_UI_BASE = "https://sign.sorahjy.com/"


const val RELINK_WEIXIN_TOKEN_LIFE_MINUTES: Long = 2
const val SET_STUDENT_INFO_TOKEN_LIFE_MINUTES: Long = 2
const val RESET_PASSWORD_TOKEN_LIFE_HOURS: Long = 24
const val INVITE_USER_TOKEN_LIFE_HOURS: Long = 24 * 3

@JvmField
val SYSTEM_ZONE: ZoneId = ZoneId.of("Asia/Shanghai")

package com.enihsyou.collaboration.server.util

import java.time.ZoneId

const val URL_BASE = "/api/"
const val TABLE_PREFIX = "co_"
const val URL_UI_BASE = "https://coll.sorahjy.com/"

/**系统时区*/
@JvmField
val SYSTEM_ZONE: ZoneId = ZoneId.of("Asia/Shanghai")

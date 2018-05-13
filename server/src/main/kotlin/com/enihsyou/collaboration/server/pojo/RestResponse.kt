package com.enihsyou.collaboration.server.pojo

enum class ResponseCode(val code: Int, val message: String) {
    OK(0, "OK")
}

open class RestResponse(
    val code: String,
    val msg: String,
    val data: Any?
) {

    constructor(code: Int, msg: String, data: Any?) : this(code.toString(), msg, data)

    constructor(responseCode: ResponseCode, data: Any?) : this(responseCode.code.toString(), responseCode.message, data)

    companion object {
        @JvmStatic
        fun ok(data: Any? = null) = RestResponse(ResponseCode.OK, data)
    }
}

class ExceptionResponse(responseCode: ResponseCode, data: Any?) : RestResponse(responseCode, data)

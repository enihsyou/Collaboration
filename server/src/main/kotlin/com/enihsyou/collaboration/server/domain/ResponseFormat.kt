package com.enihsyou.collaboration.server.domain

/**客户端的请求格式*/
open class RequestFormat<out T>(
    method_: String,
    payload_: T
) {

    /**请求方法*/
    val method: String = method_

    /**请求体*/
    val payload: T? = payload_

    /**由哪个用户提交的请求，如果是匿名用户则为空*/
    val madeBy: Long? = null
}

enum class ResponseReturnCode(val numberFormat: Int, val meaning: String) {
    OK_200(0, "everything ok"),
    PARAMETERS_400(1, "wrong parameters"),
    INTERNAL_500(2, "internal error"),
    FUNCTION_404(3, "no such function"),
}

/**服务器的返回格式*/
open class ResponseFormat<out T>(
    code_: ResponseReturnCode,
    data_: T
) {

    /**API返回值，数字形式，不是HTTP返回值*/
    val code: Int = code_.numberFormat

    /**表示状态信息的一段文字*/
    val message: String = code_.meaning

    /**payload 返回值*/
    val payload: T? = data_

    constructor(code: ResponseReturnCode, data: () -> T) :
        this(code, data())
}

class OkResponse<T>(data: () -> T) : ResponseFormat<T>(ResponseReturnCode.OK_200, data)
class RequestErrorResponse
class InternalErrorResponse
class NotFoundErrorResponse

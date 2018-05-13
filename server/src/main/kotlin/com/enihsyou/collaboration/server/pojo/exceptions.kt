package com.enihsyou.collaboration.server.pojo

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

abstract class RestRuntimeException(
    val code: Int,
    val msg: String,
    val data: Any? = null
) : RuntimeException(msg)

class NeedLoginException : RestRuntimeException(100, "用户需要登录")

class WrongArgumentException(vararg arguments: String) : RestRuntimeException(101, "[${arguments.joinToString()}]不正确")

@RestControllerAdvice
class GlobalControllerExceptionHandler {

    @ExceptionHandler(RestRuntimeException::class)
    fun handleRuntimeRestException(e: RestRuntimeException): ResponseEntity<RestResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(e.run { RestResponse(code, msg, data) })
    }
}

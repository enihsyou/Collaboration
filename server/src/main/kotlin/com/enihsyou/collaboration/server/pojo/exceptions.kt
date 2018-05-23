package com.enihsyou.collaboration.server.pojo

import com.enihsyou.collaboration.server.domain.CoPad
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

abstract class RestRuntimeException(
    val code: Int,
    val msg: String,
    val data: Any? = null
) : RuntimeException(msg)

class NeedLoginException :
    RestRuntimeException(100, "用户需要登录")

class WrongArgumentException(vararg arguments: String, adition: String = "") :
    RestRuntimeException(101, "参数[${arguments.joinToString()}]不正确。$adition")

class UserExistException(username: String) :
    RestRuntimeException(102, "用户名 [$username] 已存在")

class UserNotExistException(username: String) :
    RestRuntimeException(201, "用户名 [$username] 不存在")

class BadCredentialsException(username: String):
    RestRuntimeException(200, "用户名 [$username] 密码错误")

class PadNotExistException(padId: Long) :
    RestRuntimeException(202, "文稿 [$padId] 不存在")

class InstantNotExistException(padId: Long, revisionId: String) :
    RestRuntimeException(203, "历史记录 [$padId#$revisionId] 不存在")

class InviteLinkNotExistException(token: String) :
    RestRuntimeException(204, "邀请链接 [$token] 不存在")

class PadLockedException(pad: CoPad) :
    RestRuntimeException(301, "文稿 #${pad.id}-[${pad.title}] 锁定中")

class InviteLinkNotTargetedException(username: String) :
    RestRuntimeException(302, "邀请链接不给 [$username] 用")

class InviteLinkHasExpiredException(token: String) :
    RestRuntimeException(303, "邀请链接 [$token] 已失效")

class RangeCollapsedException(range: IntRange, collapsedWith: IntRange) :
    RestRuntimeException(401, "[$range]与区间[$collapsedWith]重叠")

class LockNotExistException(lockId: Long) :
    RestRuntimeException(454, "文稿锁 [$lockId] 不存在")

@RestControllerAdvice
class GlobalControllerExceptionHandler {

    @ExceptionHandler(RestRuntimeException::class)
    fun handleRuntimeRestException(e: RestRuntimeException): ResponseEntity<RestResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(e.run { RestResponse(code, msg, data) })
    }
}

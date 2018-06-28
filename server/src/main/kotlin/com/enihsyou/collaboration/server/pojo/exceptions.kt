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

/**用户需要登录时抛出的异常*/
class NeedLoginException :
    RestRuntimeException(100, "用户需要登录")

/**参数不正确抛出的异常*/
class WrongArgumentException(vararg arguments: String, addition: String = "") :
    RestRuntimeException(101, "参数[${arguments.joinToString()}]不正确。$addition")

/**相同用户名已存在时抛出的异常*/
class UserExistException(username: String) :
    RestRuntimeException(102, "用户名 [$username] 已存在")

/**用户名不存在时抛出的异常*/
class UserNotExistException(username: String) :
    RestRuntimeException(201, "用户名 [$username] 不存在")

/**用户提供的登录密码不正确时抛出的异常*/
class BadCredentialsException(username: String) :
    RestRuntimeException(200, "用户名 [$username] 密码错误")

/**指定的文稿id号不存在时抛出的异常*/
class PadNotExistException(padId: Long) :
    RestRuntimeException(202, "文稿 [$padId] 不存在")

/**指定的历史记录id号不存在时抛出的异常*/
class InstantNotExistException(padId: Long, revisionId: Long) :
    RestRuntimeException(203, "历史记录 [$padId#$revisionId] 不存在")

/**邀请链接不存在时抛出的异常*/
class InviteLinkNotExistException(token: String) :
    RestRuntimeException(204, "邀请链接 [$token] 不存在")

/**用户不拥有该文档时抛出的异常*/
class PadNotOwnedException(padId: Long, username: String) :
    RestRuntimeException(205, "用户 [$username] 不拥有文稿 [$padId]")

/**指定文稿段已被锁定时抛出的异常*/
class PadSegemntLockedException(pad: CoPad) :
    RestRuntimeException(301, "文稿 #${pad.id}-[${pad.title}] 锁定中")

/**用户不能使用指定的邀请链接时抛出的异常*/
class InviteLinkNotTargetedException(username: String) :
    RestRuntimeException(302, "邀请链接不给 [$username] 用")

/**邀请链接已过期时抛出的异常*/
class InviteLinkHasExpiredException(token: String) :
    RestRuntimeException(303, "邀请链接 [$token] 已失效")

/**请求的锁定段有冲突时抛出的异常*/
class RangeCollapsedException(range: IntRange, collapsedWith: IntRange) :
    RestRuntimeException(401, "[$range]与区间[$collapsedWith]重叠")

/**解锁id号不存在时抛出的异常*/
class LockNotExistException(lockId: Long) :
    RestRuntimeException(454, "文稿锁 [$lockId] 不存在")

@RestControllerAdvice
class GlobalControllerExceptionHandler {

    @ExceptionHandler(RestRuntimeException::class)
    fun handleRuntimeRestException(e: RestRuntimeException): ResponseEntity<RestResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(e.run { RestResponse(code, msg, data) })
    }
}

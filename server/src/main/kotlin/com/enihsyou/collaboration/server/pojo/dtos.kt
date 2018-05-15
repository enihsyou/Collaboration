package com.enihsyou.collaboration.server.pojo

class AccountLoginDTO(
    val username: String,
    val password: String
)

class AccountCreateDTO(
    val username: String,
    val password: String
)

class PasswordChangeDTO(
    val old_password: String,
    val new_password: String
)

class AccountInfoChangeDTO(
    /**用户的新邮件地址，null则取消原先的设定*/
    val email_address: String?
)

class PadCreateDTO(
    val title: String
)

class PadUpdateDTO(
    val title: String
)

class PadSaveDTO(
    val tag: String?
)

//// Websocket DTOs
class FetchPadDTO(
    val pad_id:Long,
    val client_revision: Long
)

class LockAcquireDTO(
    val pad_id: Long,
    val client_revision: Long,
    val range: String
)

class LockReleaseDTO(
    val pad_id: Long,
    val client_revision: Long,
    val lock_id: Long,
    val modified: Boolean,
    val replace: String?
)

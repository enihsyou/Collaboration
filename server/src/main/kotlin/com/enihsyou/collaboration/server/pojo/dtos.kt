package com.enihsyou.collaboration.server.pojo

private typealias Username = String
private typealias Password = String
private typealias EmailAddress = String
private typealias DomainId = Long
private typealias RevisionId = Long

data class AccountLoginDTO(
    val username: Username,
    val password: Password
)

data class AccountCreateDTO(
    val username: Username,
    val password: Password
)

data class PasswordChangeDTO(
    val old_password: Password,
    val new_password: Password
)

data class AccountInfoChangeDTO(
    /**用户的新邮件地址，null则取消原先的设定*/
    val email_address: EmailAddress?
)

data class PadCreateDTO(
    val title: String
)

data class PadUpdateDTO(
    val title: String
)

data class PadSaveDTO(
    val tag: String?
)

//// Websocket DTOs
data class FetchPadDTO(
    val pad_id: DomainId,
    val client_revision: RevisionId
)

data class LockAcquireDTO(
    val pad_id: DomainId,
    val client_revision: RevisionId,
    val range: IntRange
)

data class LockReleaseDTO(
    val pad_id: DomainId,
    val client_revision: RevisionId,
    val lock_id: DomainId,
    val modified: Boolean,
    val replacement: String?
)

//// Program use
data class DiffResult(
    val from: String
)

package com.enihsyou.collaboration.server.pojo

class AccountLoginDTO(
    val username: String,
    val password: String
)

class AccountRegisteDTO(
    val username: String,
    val password: String
)

class PasswordChangeDTO(
    val old_password: String,
    val new_password: String
)

class AccountInfoChangeDTO(
    /**用户的新邮件地址，Null则取消原先的设定*/
    val email_address: String?
)

class PasswordResetDTO

class CabinetCreateDTO

class CabinetUpdateDTO

class PadCreateDTO

class PadUpdateDTO

class PadSaveDTO(
    val tag:String?
)
//// Websocket DTOs
class FetchPadDTO(

)

class LockPadDTO(

)

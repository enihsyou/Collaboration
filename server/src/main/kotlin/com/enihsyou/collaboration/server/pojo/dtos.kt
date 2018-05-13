package com.enihsyou.collaboration.server.pojo

class AccountLoginDTO(
    val username: String,
    val password: String
)

class AccountRegisteDTO

class PasswordChangeDTO(
    val old_password: String,
    val new_password: String
)

class AccountInfoChangeDTO

class PasswordResetDTO

class CabinetCreateDTO

class CabinetUpdateDTO

class PadCreateDTO

class PadUpdateDTO


//// Websocket DTOs
class FetchPadDTO(

)
class LockPadDTO(

)

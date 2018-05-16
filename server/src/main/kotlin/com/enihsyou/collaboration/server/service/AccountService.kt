package com.enihsyou.collaboration.server.service

import com.enihsyou.collaboration.server.domain.CoIndividual
import com.enihsyou.collaboration.server.pojo.AccountCreateDTO
import com.enihsyou.collaboration.server.pojo.AccountInfoChangeDTO
import com.enihsyou.collaboration.server.pojo.AccountLoginDTO
import com.enihsyou.collaboration.server.pojo.PasswordChangeDTO

/** 处理用户信息相关的逻辑  */
interface AccountService {

    /** 提供用户名和密码，来创建账号  */
    fun createAccount(accountCreateDTO: AccountCreateDTO): CoIndividual

    /** 处理账户登录  */
    fun loginAccount(accountLoginDTO: AccountLoginDTO): CoIndividual

    /**
     * 修改账户信息
     *
     * @param account 目标账号
     */
    fun changeInfo(accountInfoChangeDTO: AccountInfoChangeDTO, account: CoIndividual): CoIndividual

    /**
     * 修改账户密码
     *
     * @param account 目标账号
     */
    fun changePassword(passwordChangeDTO: PasswordChangeDTO, account: CoIndividual): CoIndividual

    /** 为用户名创建找回密码的凭据  */
    fun resetPassword(username: String): CoIndividual
}



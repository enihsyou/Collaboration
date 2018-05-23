package com.enihsyou.collaboration.server.service

import com.enihsyou.collaboration.server.domain.CoIndividual
import com.enihsyou.collaboration.server.pojo.AccountCreateDTO
import com.enihsyou.collaboration.server.pojo.AccountInfoChangeDTO
import com.enihsyou.collaboration.server.pojo.AccountLoginDTO
import com.enihsyou.collaboration.server.pojo.PasswordChangeDTO

/** 处理用户信息相关的逻辑  */
interface AccountService {

    /**
     * 提供用户名和密码，来创建账号
     *
     * 首先检查用户名是否重复
     * 再创建数据库对象实体
     * 最后保存到数据库
     * */
    fun createAccount(accountCreateDTO: AccountCreateDTO): CoIndividual

    /**
     * 处理账户登录
     *
     * 验证密码是否匹配
     * */
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
     * 首先验证旧密码匹配
     * 然后把新密码保存进去
     *
     * @param account 目标账号
     */
    fun changePassword(passwordChangeDTO: PasswordChangeDTO, account: CoIndividual): CoIndividual

    /** 为用户名创建找回密码的凭据
     *
     * 验证用户名是否存在，不存在直接跳过后面的处理
     * 没有验证邮箱的话，也返回错误
     * 生成一个找回密钥，发送到邮箱里去
     *
     * @param username 找回密码的用户名*/
    fun resetPassword(username: String): CoIndividual
}



package com.enihsyou.collaboration.server.service;

import com.enihsyou.collaboration.server.domain.CoIndividual;
import com.enihsyou.collaboration.server.pojo.AccountCreateDTO;
import com.enihsyou.collaboration.server.pojo.AccountInfoChangeDTO;
import com.enihsyou.collaboration.server.pojo.AccountLoginDTO;
import com.enihsyou.collaboration.server.pojo.PasswordChangeDTO;

/** 处理用户信息相关的逻辑 */
public interface AccountService {

    /** 提供用户名和密码，来创建账号 */
    CoIndividual createAccount(final AccountCreateDTO accountCreateDTO);

    /**
     * 修改账户信息
     *
     * @param account 目标账号
     */
    CoIndividual changeInfo(final AccountInfoChangeDTO accountInfoChangeDTO, final CoIndividual account);

    /**
     * 修改账户密码
     *
     * @param account 目标账号
     */
    CoIndividual changePassword(final PasswordChangeDTO passwordChangeDTO, final CoIndividual account);

    /** 处理账户登录 */
    CoIndividual loginAccount(final AccountLoginDTO accountLoginDTO);

    /** 为用户名创建找回密码的凭据 */
    CoIndividual resetPassword(String username);
}


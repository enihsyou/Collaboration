package com.enihsyou.collaboration.server.service;

import com.enihsyou.collaboration.server.domain.CoIndividual;
import com.enihsyou.collaboration.server.pojo.AccountInfoChangeDTO;
import com.enihsyou.collaboration.server.pojo.AccountLoginDTO;
import com.enihsyou.collaboration.server.pojo.AccountRegisteDTO;
import com.enihsyou.collaboration.server.pojo.PasswordChangeDTO;

/** 处理用户信息相关的逻辑 */
public interface AccountService {

    CoIndividual createAccount(AccountRegisteDTO accountCreateDTO);

    CoIndividual changeInfo(AccountInfoChangeDTO accountInfoChangeDTO);

    CoIndividual changePassword(PasswordChangeDTO passwordChangeDTO);

    CoIndividual loginAccount(AccountLoginDTO accountLoginDTO);
}


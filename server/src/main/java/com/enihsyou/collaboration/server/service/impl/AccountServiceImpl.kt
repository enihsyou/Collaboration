package com.enihsyou.collaboration.server.service.impl

import com.enihsyou.collaboration.server.authentication.CoIndividualUserDetailsAdapter
import com.enihsyou.collaboration.server.authentication.IndividualAuthenticationProvider
import com.enihsyou.collaboration.server.domain.CoIndividual
import com.enihsyou.collaboration.server.pojo.AccountCreateDTO
import com.enihsyou.collaboration.server.pojo.AccountInfoChangeDTO
import com.enihsyou.collaboration.server.pojo.AccountLoginDTO
import com.enihsyou.collaboration.server.pojo.PasswordChangeDTO
import com.enihsyou.collaboration.server.service.AccountService
import com.enihsyou.collaboration.server.util.PermissionUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service

@Service
class AccountServiceImpl @Autowired
constructor(private val authenticationProvider: IndividualAuthenticationProvider) : AccountService {

    override fun createAccount(accountCreateDTO: AccountCreateDTO): CoIndividual? {
        return null
    }

    override fun changeInfo(accountInfoChangeDTO: AccountInfoChangeDTO): CoIndividual? {
        return null
    }

    override fun changePassword(passwordChangeDTO: PasswordChangeDTO): CoIndividual {
        val account = PermissionUtils.loggedAccount()

        val oldPassword = passwordChangeDTO.old_password
        val newPassword = passwordChangeDTO.new_password

        val authentication = authenticationProvider.authenticate(
            UsernamePasswordAuthenticationToken(account.username, oldPassword)
        )

        return (authentication.details as CoIndividualUserDetailsAdapter)
            .account.setPassword(authenticationProvider.passwordEncoder.encode(newPassword))
    }

    override fun loginAccount(accountLoginDTO: AccountLoginDTO): CoIndividual {

        val username = accountLoginDTO.username
        val password = accountLoginDTO.password

        val authentication = authenticationProvider.authenticate(
            UsernamePasswordAuthenticationToken(username, password)
        )

        return (authentication.details as CoIndividualUserDetailsAdapter).account
    }

    companion object {

        private val LOGGER = LoggerFactory.getLogger(AccountService::class.java)
    }
}

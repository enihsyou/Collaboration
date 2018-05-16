package com.enihsyou.collaboration.server.service.impl

import com.enihsyou.collaboration.server.authentication.CoIndividualUserDetailsAdapter
import com.enihsyou.collaboration.server.authentication.IndividualAuthenticationProvider
import com.enihsyou.collaboration.server.domain.CoCabinet
import com.enihsyou.collaboration.server.domain.CoIndividual
import com.enihsyou.collaboration.server.pojo.AccountCreateDTO
import com.enihsyou.collaboration.server.pojo.AccountInfoChangeDTO
import com.enihsyou.collaboration.server.pojo.AccountLoginDTO
import com.enihsyou.collaboration.server.pojo.PasswordChangeDTO
import com.enihsyou.collaboration.server.pojo.UserExistException
import com.enihsyou.collaboration.server.pojo.UserNotExistException
import com.enihsyou.collaboration.server.repository.IndividualRepository
import com.enihsyou.collaboration.server.service.AccountService
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class AccountServiceImpl(
    private val authenticationProvider: IndividualAuthenticationProvider,
    private val individualRepository: IndividualRepository
) : AccountService {

    override fun createAccount(accountCreateDTO: AccountCreateDTO): CoIndividual {
        val (username, password) = accountCreateDTO

        /*检查重复用户名*/
        if (individualRepository.findByUsername(username) !== null) {
            throw UserExistException(username)
        }

        /*创建对象实体*/
        val account = CoIndividual()
            .setUsername(username)
            .setPassword(authenticationProvider.encodePassword(password))
        val cabinet = CoCabinet()
            .setBelongTo(account)
        account.cabinet = cabinet

        /*保存到数据库*/
        individualRepository.save(account)

        return account
    }

    override fun loginAccount(accountLoginDTO: AccountLoginDTO): CoIndividual {
        val (username, password) = accountLoginDTO

        /*验证密码匹配*/
        val authentication = authenticationProvider
            .authenticate(UsernamePasswordAuthenticationToken(username, password))

        /*设置登录成功凭据*/
        SecurityContextHolder.getContext().authentication = authentication

        /*设置最后登录时间*/
        val account = (authentication.details as CoIndividualUserDetailsAdapter).account
        account.lastLoginTime = Instant.now()

        return account
    }

    override fun changeInfo(accountInfoChangeDTO: AccountInfoChangeDTO, account: CoIndividual): CoIndividual {
        val (email) = accountInfoChangeDTO

        /*修改为新的值*/
        account.emailAddress = email

        return account
    }

    override fun changePassword(passwordChangeDTO: PasswordChangeDTO, account: CoIndividual): CoIndividual {
        val (oldPassword, newPassword) = passwordChangeDTO

        /*验证密码匹配*/
        authenticationProvider.authenticate(UsernamePasswordAuthenticationToken(account.username, oldPassword))

        /*加密密码*/
        val encodePassword = authenticationProvider.encodePassword(newPassword)

        /*保存密码*/
        return account.setPassword(encodePassword)
    }

    override fun resetPassword(username: String): CoIndividual {
        /*检查用户名存在*/
        if (individualRepository.findByUsername(username) === null) {
            throw UserNotExistException(username)
        }

        TODO() // todo
    }

    companion object {
        val LOGGER = LoggerFactory.getLogger(AccountService::class.java)
    }
}

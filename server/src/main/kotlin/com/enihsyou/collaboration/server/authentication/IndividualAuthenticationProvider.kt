package com.enihsyou.collaboration.server.authentication

import com.enihsyou.collaboration.server.domain.CoIndividual
import com.enihsyou.collaboration.server.repository.IndividualRepository
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

/**使用这个类进行用户名密码的验证*/
@Component
class IndividualAuthenticationProvider(
   private val individualRepository: IndividualRepository,
    val passwordEncoder: PasswordEncoder
) : AbstractUserDetailsAuthenticationProvider() {

    override fun retrieveUser(username: String, authentication: UsernamePasswordAuthenticationToken): UserDetails {
        val account = individualRepository.findByUsername(username)
            ?: throw UsernameNotFoundException(username)

        return CoIndividualUserDetailsAdapter(account)
    }

    override fun additionalAuthenticationChecks(
        userDetails: UserDetails,
        authentication: UsernamePasswordAuthenticationToken
    ) {
        val presentedPassword = authentication.credentials.toString()

        if (!passwordEncoder.matches(presentedPassword, userDetails.password)) {
            logger.info("[${userDetails.username}]密码验证失败")
            throw BadCredentialsException(userDetails.username)
        }
        authentication.details = userDetails
    }
}

class CoIndividualUserDetailsAdapter(
    val account: CoIndividual
) : UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = mutableSetOf()

    override fun isEnabled(): Boolean = true

    override fun getUsername(): String = account.username

    override fun isCredentialsNonExpired(): Boolean = true

    override fun getPassword(): String = account.password

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true
}

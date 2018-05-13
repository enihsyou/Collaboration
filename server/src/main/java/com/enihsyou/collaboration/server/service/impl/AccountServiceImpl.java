package com.enihsyou.collaboration.server.service.impl;

import com.enihsyou.collaboration.server.authentication.CoIndividualUserDetailsAdapter;
import com.enihsyou.collaboration.server.authentication.IndividualAuthenticationProvider;
import com.enihsyou.collaboration.server.domain.CoIndividual;
import com.enihsyou.collaboration.server.pojo.AccountInfoChangeDTO;
import com.enihsyou.collaboration.server.pojo.AccountLoginDTO;
import com.enihsyou.collaboration.server.pojo.AccountRegisteDTO;
import com.enihsyou.collaboration.server.pojo.PasswordChangeDTO;
import com.enihsyou.collaboration.server.service.AccountService;
import com.enihsyou.collaboration.server.util.PermissionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

    private final IndividualAuthenticationProvider authenticationProvider;

    @Autowired
    public AccountServiceImpl(final IndividualAuthenticationProvider authenticationProvider) {this.authenticationProvider = authenticationProvider;}

    @Override
    public CoIndividual createAccount(final AccountRegisteDTO accountCreateDTO) {
        return null;
    }

    @Override
    public CoIndividual changeInfo(final AccountInfoChangeDTO accountInfoChangeDTO) {
        return null;
    }

    @Override
    public CoIndividual changePassword(final PasswordChangeDTO passwordChangeDTO) {
        final CoIndividual account = PermissionUtils.loggedAccount();

        String oldPassword = passwordChangeDTO.getOld_password();
        String newPassword = passwordChangeDTO.getNew_password();

        final Authentication authentication = authenticationProvider.authenticate(
            new UsernamePasswordAuthenticationToken(account.getUsername(), oldPassword));

        return ((CoIndividualUserDetailsAdapter) authentication.getDetails())
            .getAccount().setPassword(authenticationProvider.getPasswordEncoder().encode(newPassword));
    }

    @Override
    public CoIndividual loginAccount(final AccountLoginDTO accountLoginDTO) {

        String username = accountLoginDTO.getUsername();
        String password = accountLoginDTO.getPassword();

        final Authentication authentication = authenticationProvider.authenticate(
            new UsernamePasswordAuthenticationToken(username, password));

        return ((CoIndividualUserDetailsAdapter) authentication.getDetails()).getAccount();
    }
}

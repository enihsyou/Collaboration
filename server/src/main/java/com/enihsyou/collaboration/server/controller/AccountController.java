package com.enihsyou.collaboration.server.controller;

import com.enihsyou.collaboration.server.domain.CoIndividual;
import com.enihsyou.collaboration.server.domain.ExtensionsKt;
import com.enihsyou.collaboration.server.pojo.AccountInfoChangeDTO;
import com.enihsyou.collaboration.server.pojo.AccountLoginDTO;
import com.enihsyou.collaboration.server.pojo.AccountRegisteDTO;
import com.enihsyou.collaboration.server.pojo.PasswordChangeDTO;
import com.enihsyou.collaboration.server.pojo.RestResponse;
import com.enihsyou.collaboration.server.service.AccountService;
import com.enihsyou.collaboration.server.util.DetailLevel;
import com.enihsyou.collaboration.server.util.PermissionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** 用户账户相关的控制器 */
@RestController
@RequestMapping("account")
public class AccountController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);

    private final AccountService accountService;

    public AccountController(final AccountService accountService) {this.accountService = accountService;}

    /** 用户登录 */
    @PostMapping("login")
    public RestResponse loginToken(@RequestBody AccountLoginDTO accountLoginDTO) {
        CoIndividual account = accountService.loginAccount(accountLoginDTO);
        return RestResponse.ok(ExtensionsKt.toLoginVO(account));
    }

    /** 创建一个账户*/
    @PostMapping
    public RestResponse create(@RequestBody AccountRegisteDTO accountCreateDTO) {

        CoIndividual account = accountService.createAccount(accountCreateDTO);

        return RestResponse.ok(ExtensionsKt.toCreateVO(account));
    }

    /** 获取账户信息 */
    @GetMapping
    public RestResponse getInfo(@RequestParam(required = false, defaultValue = DetailLevel.LEVEL_BRIEF) String level) {
        LOGGER.debug(""); // todo add logger

        CoIndividual account = PermissionUtils.loggedAccount();

        return RestResponse.ok(ExtensionsKt.toInfoVO(account, level));
    }

    /** 修改账户信息 */
    @PutMapping
    public RestResponse changeInfo(@RequestBody AccountInfoChangeDTO accountInfoChangeDTO) {

        CoIndividual account = accountService.changeInfo(accountInfoChangeDTO);

        return RestResponse.ok(ExtensionsKt.toInfoChangeVO(account));
    }

    /** 修改账户密码 */
    @PutMapping("password")
    public RestResponse changePassword(@RequestBody PasswordChangeDTO passwordChangeDTO) {

        CoIndividual account = accountService.changePassword(passwordChangeDTO);

        return RestResponse.ok(ExtensionsKt.toChangePasswordVO(account));
    }
}

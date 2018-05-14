package com.enihsyou.collaboration.server.controller;

import com.enihsyou.collaboration.server.domain.CoIndividual;
import com.enihsyou.collaboration.server.domain.ExtensionsKt;
import com.enihsyou.collaboration.server.pojo.*;
import com.enihsyou.collaboration.server.service.AccountService;
import com.enihsyou.collaboration.server.util.DetailLevel;
import com.enihsyou.collaboration.server.util.PermissionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

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
        // todo add logger

        CoIndividual account = accountService.loginAccount(accountLoginDTO);

        return RestResponse.ok(ExtensionsKt.toLoginVO(account));
    }

    /** 创建一个账户 */
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
        final DetailLevel detailLevel = DetailLevel.parseLevel(level);

        return RestResponse.ok(ExtensionsKt.toDetailVO(account, detailLevel));
    }

    /**
     * 修改账户信息
     *
     * @see AccountInfoChangeDTO
     */
    @PutMapping
    public RestResponse changeInfo(@RequestBody AccountInfoChangeDTO accountInfoChangeDTO) {

        CoIndividual account = accountService.changeInfo(accountInfoChangeDTO);

        return RestResponse.ok(ExtensionsKt.toInfoChangeVO(account));
    }

    /**
     * 修改账户密码
     *
     * @see PasswordChangeDTO
     */
    @PutMapping("password")
    public RestResponse changePassword(@RequestBody PasswordChangeDTO passwordChangeDTO) {

        CoIndividual account = accountService.changePassword(passwordChangeDTO);

        return RestResponse.ok(ExtensionsKt.toChangePasswordVO(account));
    }
}

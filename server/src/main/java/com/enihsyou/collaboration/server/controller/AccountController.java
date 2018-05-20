package com.enihsyou.collaboration.server.controller;

import com.enihsyou.collaboration.server.domain.CoIndividual;
import com.enihsyou.collaboration.server.domain.ExtensionsKt;
import com.enihsyou.collaboration.server.pojo.*;
import com.enihsyou.collaboration.server.service.AccountService;
import com.enihsyou.collaboration.server.service.PermissionService;
import com.enihsyou.collaboration.server.service.PermissionUtil;
import com.enihsyou.collaboration.server.util.DetailLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/** 用户账户相关的控制器 */
@RestController
@RequestMapping("account")
public class AccountController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);

    private final AccountService accountService;

    private final PermissionService permissionService;

    public AccountController(final AccountService accountService, final PermissionService permissionService) {
        this.accountService = accountService;
        this.permissionService = permissionService;
    }

    /** 用户登录 */
    @PostMapping("login")
    public RestResponse loginToken(@RequestBody AccountLoginDTO accountLoginDTO) {
        LOGGER.debug("用户登录 [{}]", accountLoginDTO.getUsername());

        CoIndividual account = accountService.loginAccount(accountLoginDTO);

        return RestResponse.ok(ExtensionsKt.toLoginVO(account));
    }

    /** 创建一个账户 */
    @PostMapping
    public RestResponse create(@RequestBody AccountCreateDTO accountCreateDTO) {
        LOGGER.debug("用户注册 [{}]", accountCreateDTO.getUsername());

        CoIndividual account = accountService.createAccount(accountCreateDTO);

        return RestResponse.ok(ExtensionsKt.toCreateVO(account));
    }

    /** 获取账户信息 */
    @GetMapping
    public RestResponse getInfo(@RequestParam(required = false, defaultValue = DetailLevel.LEVEL_BRIEF) String level) {
        LOGGER.debug("获取用户信息 [{}] detail level: {}", PermissionUtil.currentUsername(), level);

        CoIndividual account = permissionService.loggedAccount();

        final DetailLevel detailLevel = DetailLevel.parseLevel(level);

        return RestResponse.ok(ExtensionsKt.toDetailVO(account, detailLevel));
    }

    /** 修改账户信息 */
    @PutMapping
    public RestResponse changeInfo(@RequestBody AccountInfoChangeDTO accountInfoChangeDTO) {
        LOGGER.debug("修改用户信息 [{}] email: {}", PermissionUtil.currentUsername(),
            accountInfoChangeDTO.getEmail_address());

        CoIndividual account = permissionService.loggedAccount();

        accountService.changeInfo(accountInfoChangeDTO, account);

        return RestResponse.ok(ExtensionsKt.toInfoChangeVO(account));
    }

    /** 修改账户密码 */
    @PutMapping("password")
    public RestResponse changePassword(@RequestBody PasswordChangeDTO passwordChangeDTO) {
        LOGGER.debug("用户修改密码 [{}]", PermissionUtil.currentUsername());

        final CoIndividual account = permissionService.loggedAccount();

        accountService.changePassword(passwordChangeDTO, account);

        return RestResponse.ok(ExtensionsKt.toChangePasswordVO(account));
    }

    // /** 找回账户密码 */
    // @PostMapping("reset")
    // public RestResponse resetPassword(@RequestParam String username) {
    //     LOGGER.debug("用户找回密码 username: {}", username);
    //
    //     CoIndividual account = accountService.resetPassword(username);
    //
    //     return RestResponse.ok(ExtensionsKt.toResetPasswordVO(account));
    // }
}

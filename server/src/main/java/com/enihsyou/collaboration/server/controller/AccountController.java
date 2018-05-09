package com.enihsyou.collaboration.server.controller;

import com.enihsyou.collaboration.server.domain.CoIndividual;
import com.enihsyou.collaboration.server.domain.ExtensionsKt;
import com.enihsyou.collaboration.server.pojo.AccountLoginDTO;
import com.enihsyou.collaboration.server.pojo.AccountRegisteDTO;
import com.enihsyou.collaboration.server.pojo.PasswordChangeDTO;
import com.enihsyou.collaboration.server.service.AccountService;
import com.enihsyou.collaboration.server.util.INFO_DETAIL_LEVEL;
import com.enihsyou.collaboration.server.util.PermissionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/** 用户账户相关的控制器 */
@RestController
@RequestMapping("account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(final AccountService accountService) {this.accountService = accountService;}

    /** 创建一个账户 */
    @PostMapping
    public Map<String, Object> create(@RequestBody AccountRegisteDTO accountCreateDTO) {
        CoIndividual account = accountService.accountCreate(accountCreateDTO);
        return ExtensionsKt.toCreateVO(account);
    }

    /** 修改账户密码 */
    @PutMapping("password")
    public void changePassword(@RequestBody PasswordChangeDTO passwordChangeDTO) {
        CoIndividual account = accountService.changeAccountPassword(passwordChangeDTO);
        return ExtensionsKt.toChangePasswordVO(account);
    }

    /** 获取账户信息 */
    @GetMapping("info")
    public Map<String, Object> info(@RequestParam(required = false, defaultValue = "brief") String level) {
        LOGGER.debug(""); // todo add logger
        CoIndividual account = PermissionUtils.INSTANCE.loggedAccount();
        return ExtensionsKt.toInfoVO(level);
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);
}



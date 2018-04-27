package com.enihsyou.collaboration.server.controller;

import com.enihsyou.collaboration.server.dto.AccountLoginDTO;
import com.enihsyou.collaboration.server.dto.AccountRegisteDTO;
import com.enihsyou.collaboration.server.dto.PasswordChangeDTO;
import com.enihsyou.collaboration.server.service.AccountService;
import com.enihsyou.collaboration.server.vo.AccountInfoVO;
import kotlin.NotImplementedError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** 用户账户相关的控制器 */
@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(final AccountService accountService) {this.accountService = accountService;}

    /** 创建一个账户 */
    @PostMapping
    public void create(@RequestBody AccountRegisteDTO accountCreateDTO) {

    }

    /** 用户登录 */
    @PostMapping("login")
    public void login(@RequestBody AccountLoginDTO accountLoginDTO) {

    }

    /** 修改账户密码 */
    @PutMapping("password")
    public void changePassword(@RequestBody PasswordChangeDTO passwordChangeDTO) {

    }

    /** 获取账户信息 */
    @GetMapping("info")
    public AccountInfoVO info(@RequestParam(required = false, defaultValue = "brief") String level) {
        throw new NotImplementedError();
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);
}

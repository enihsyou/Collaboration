package com.enihsyou.collaboration.server.controller;

import com.enihsyou.collaboration.server.domain.CoIndividual;
import com.enihsyou.collaboration.server.domain.ExtensionsKt;
import com.enihsyou.collaboration.server.service.AccountService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("token")
public class TokenController {

    private final AccountService accountService;

    public TokenController(final AccountService accountService) {this.accountService = accountService;}

    /** 用户登录 */
    @PostMapping("login")
    public Map<String, Object> loginToken() {
        CoIndividual account = accountService.accountLogin(accountLoginDTO);
        return ExtensionsKt.toLoginVO(account);
    }

    @PostMapping("join")
    public void joinToken(@RequestParam String token) {}
}

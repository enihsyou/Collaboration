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

    /** 日志记录器 */
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);

    /** 执行用户账户相关的逻辑 */
    private final AccountService accountService;

    /** 进行用户权限控制 */
    private final PermissionService permissionService;

    /** Autowired 依赖注入构造器 */
    public AccountController(final AccountService accountService, final PermissionService permissionService) {
        this.accountService = accountService;
        this.permissionService = permissionService;
    }


    /**
     * 预览获取用户信息的接口
     * <p>
     * 用于快速测试账户的存在性和获取账户最简单信息。不需要权限。
     * 不会抛出错误，如果不存在只会返回null
     *
     * @see ExtensionsKt#toPreviewAccountVO(CoIndividual)
     */
    @GetMapping("info")
    public RestResponse previewAccount(@RequestParam String username) {
        LOGGER.debug("寻找用户 [{}] search for: {}", PermissionUtil.currentUsername(), username);

        CoIndividual account = accountService.previewAccount(username);

        return RestResponse.ok(ExtensionsKt.toPreviewAccountVO(account));
    }

    /**
     * 用户登录。
     * <p>
     * 使用用户名和密码作为登录凭据，进行登录的操作。
     * 传输过程中密码使用明文传输，通过提供客户端和服务器之间SSL通道保证通信安全。
     * <p>
     * 登录成功会给当前session设置验证凭据。
     * 如果登录失败会返回错误
     * {@link AccountLoginDTO}
     *
     * @throws com.enihsyou.collaboration.server.pojo.UserNotExistException   用户名不存在
     * @throws com.enihsyou.collaboration.server.pojo.BadCredentialsException 提供的用户名密码错误
     * @see com.enihsyou.collaboration.server.pojo.AccountLoginDTO
     */
    @PostMapping("login")
    public RestResponse loginToken(@RequestBody AccountLoginDTO accountLoginDTO) {
        LOGGER.debug("用户登录 [{}]", accountLoginDTO.getUsername());

        CoIndividual account = accountService.loginAccount(accountLoginDTO);

        return RestResponse.ok(ExtensionsKt.toLoginVO(account));
    }

    /**
     * 创建一个账户。
     * <p>
     * 使用用户名和密码注册一个新账号。
     * 传输过程中密码使用明文传输，通过提供客户端和服务器之间SSL通道保证通信安全。
     * 不对传输过程中的用户名和密码长度做限制，数据库中默认最长255字符。
     * 密码在数据库中使用{@code BCrypt}处理后保存
     * <p>
     * 如果存在相同的用户名，则提出错误。
     *
     * @throws com.enihsyou.collaboration.server.pojo.UserExistException 用户名已存在
     * @see com.enihsyou.collaboration.server.pojo.AccountCreateDTO
     * @see org.springframework.security.crypto.bcrypt.BCrypt
     */
    @PostMapping
    public RestResponse create(@RequestBody AccountCreateDTO accountCreateDTO) {
        LOGGER.debug("用户注册 [{}]", accountCreateDTO.getUsername());

        CoIndividual account = accountService.createAccount(accountCreateDTO);

        return RestResponse.ok(ExtensionsKt.toCreateVO(account));
    }

    /**
     * 获取账户信息。
     * <p>
     * 用户登录成功以后，使用这个接口获取用户的账户信息。
     *
     * @param level 获取信息的详细等级。
     *              有两个级别 {@link DetailLevel#BRIEF }简要和{@link DetailLevel#DETAIL}详细
     *              简要只给出一些简单的用户信息，比如用户id和用户名，邮箱。
     *              详细给出包括加入的文稿信息和共享等级
     *
     * @throws com.enihsyou.collaboration.server.pojo.NeedLoginException 用户未登录
     */
    @GetMapping
    public RestResponse getInfo(@RequestParam(required = false, defaultValue = DetailLevel.LEVEL_BRIEF) String level) {
        LOGGER.debug("获取用户信息 [{}] detail level: {}", PermissionUtil.currentUsername(), level);

        CoIndividual account = permissionService.loggedAccount();

        final DetailLevel detailLevel = DetailLevel.parseLevel(level);

        return RestResponse.ok(ExtensionsKt.toDetailVO(account, detailLevel));
    }

    /**
     * 修改账户信息。
     * <p>
     * 已登录的用户通过访问这个接口修改账户普通的值信息。
     * 比如修改找回密码的邮箱地址。
     *
     * @throws com.enihsyou.collaboration.server.pojo.NeedLoginException 用户未登录
     */
    @PutMapping
    public RestResponse changeInfo(@RequestBody AccountInfoChangeDTO accountInfoChangeDTO) {
        LOGGER.debug("修改用户信息 [{}] email: {}", PermissionUtil.currentUsername(),
            accountInfoChangeDTO.getEmail_address());

        CoIndividual account = permissionService.loggedAccount();

        accountService.changeInfo(accountInfoChangeDTO, account);

        return RestResponse.ok(ExtensionsKt.toInfoChangeVO(account));
    }

    /**
     * 修改账户密码
     * <p>
     * 已登录的用户发送原密码和新密码，将当前密码修改为新密码并保存生效。
     * 旧的session不会被移除，将继续有效，直到session失效期到了。
     * 密码使用明文传输。
     * @throws com.enihsyou.collaboration.server.pojo.NeedLoginException 用户未登录
     */
    @PutMapping("password")
    public RestResponse changePassword(@RequestBody PasswordChangeDTO passwordChangeDTO) {
        LOGGER.debug("用户修改密码 [{}]", PermissionUtil.currentUsername());

        final CoIndividual account = permissionService.loggedAccount();

        accountService.changePassword(passwordChangeDTO, account);

        return RestResponse.ok(ExtensionsKt.toChangePasswordVO(account));
    }

    /** 找回账户密码 */
    @PostMapping("reset")
    public RestResponse resetPassword(@RequestParam String username) {
        LOGGER.debug("用户找回密码 username: {}", username);

        CoIndividual account = accountService.resetPassword(username);

        return RestResponse.ok(ExtensionsKt.toResetPasswordVO(account));
    }
}

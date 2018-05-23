package com.enihsyou.collaboration.server.controller;

import com.enihsyou.collaboration.server.domain.CoIndividual;
import com.enihsyou.collaboration.server.domain.CoLock;
import com.enihsyou.collaboration.server.domain.CoPad;
import com.enihsyou.collaboration.server.domain.ExtensionsKt;
import com.enihsyou.collaboration.server.pojo.FetchPadDTO;
import com.enihsyou.collaboration.server.pojo.LockAcquireDTO;
import com.enihsyou.collaboration.server.pojo.LockReleaseDTO;
import com.enihsyou.collaboration.server.pojo.RestResponse;
import com.enihsyou.collaboration.server.service.PermissionService;
import com.enihsyou.collaboration.server.service.PermissionUtil;
import com.enihsyou.collaboration.server.service.WebsocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/** 处理前后端在文档和🔒更新上的交互 */
@RestController
public class WebsocketController {

    /** 日志记录器 */
    private static final Logger LOGGER = LoggerFactory.getLogger(WebsocketController.class);

    /** 进行用户权限控制 */
    private final PermissionService permissionService;

    /** 执行Websocket相关逻辑操作 */
    private final WebsocketService websocketService;

    /** 发送Websocket响应的处理器 */
    private final SimpMessageSendingOperations template;

    /** Autowired 依赖注入构造器 */
    public WebsocketController(final PermissionService permissionService,
                               final WebsocketService websocketService,
                               final SimpMessageSendingOperations template) {
        this.permissionService = permissionService;
        this.websocketService = websocketService;
        this.template = template;
    }
    // todo move to command pattern

    /** 刷新获取最新状态 */
    @MessageMapping("pad.fetch")
    @PostMapping("websocket.pad.fetch")
    public RestResponse fetchPadStatus(@RequestBody FetchPadDTO fetchPadDTO) {
        final String username = PermissionUtil.currentUsername();
        LOGGER.debug("获取文稿状态 [{}] pad: #{} revision: {}", username, fetchPadDTO.getPad_id(),
            fetchPadDTO.getClient_revision());

        final CoIndividual account = permissionService.loggedAccount();

        CoPad pad = websocketService.fetchStatus(fetchPadDTO);

        final RestResponse payload = RestResponse.ok(ExtensionsKt.socketPadFetchVO(pad));
        template.convertAndSendToUser(account.getUsername(), "", payload);
        return payload;
    }

    /**
     * 申请一篇文稿的🔒
     *
     * 需要提供文稿号和客户端当前的版本号，以及需要锁定的位置。
     *
     * @throws com.enihsyou.collaboration.server.pojo.RangeCollapsedException 锁定范围有重叠
     */
    @MessageMapping("pad.lock.acquire")
    @PostMapping("websocket.pad.lock.acquire")
    public RestResponse acquirePadLock(@RequestBody LockAcquireDTO lockAcquireDTO) {
        final String username = PermissionUtil.currentUsername();
        LOGGER.debug("尝试获取文稿🔒 [{}] pad: #{} revision: {} range: {}", username, lockAcquireDTO.getPad_id(),
            lockAcquireDTO.getClient_revision(), lockAcquireDTO.getRange());

        final CoIndividual account = permissionService.loggedAccount();

        CoLock lock = websocketService.acquireLock(lockAcquireDTO, account);

        final RestResponse payload = RestResponse.ok(ExtensionsKt.socketLockAcquireVO(lock));
        template.convertAndSendToUser(account.getUsername(), "", payload);
        return payload;
    }

    /**
     * 释放文稿的一个🔒
     *
     * 需要提供🔒的id和用户当前文稿号和版本号，以及是否有修改。
     * 如果有修改，需要同时给出修改后的结果
     */
    @MessageMapping("pad.lock.release")
    @PostMapping("websocket.pad.lock.release")
    public RestResponse releasePadLock(@RequestBody LockReleaseDTO lockReleaseDTO) {
        final String username = PermissionUtil.currentUsername();
        LOGGER.debug("释放文稿🔒 [{}] pad: #{} revision: {} lock_id: {} modified: {}", username, lockReleaseDTO.getPad_id(),
            lockReleaseDTO.getClient_revision(), lockReleaseDTO.getLock_id(), lockReleaseDTO.getModified());
        final CoIndividual account = permissionService.loggedAccount();

        CoPad pad = websocketService.releaseLock(lockReleaseDTO, account);

        final RestResponse payload = RestResponse.ok(ExtensionsKt.socketLockReleasedVO(pad));
        template.convertAndSendToUser(account.getUsername(), "", payload);
        return payload;
    }

    /** 异常处理 */
    @MessageExceptionHandler
    public void handleException(Throwable exception) {

    }
}

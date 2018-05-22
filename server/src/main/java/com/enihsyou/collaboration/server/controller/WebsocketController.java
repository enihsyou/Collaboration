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

    private static final Logger LOGGER = LoggerFactory.getLogger(WebsocketController.class);

    private final PermissionService permissionService;

    private final WebsocketService websocketService;

    private final SimpMessageSendingOperations template;

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

    /** 申请🔒 */
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

    /** 释放🔒 */
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

    @MessageExceptionHandler
    public void handleException(Throwable exception) {

    }
}

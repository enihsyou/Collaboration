package com.enihsyou.collaboration.server.controller;

import com.enihsyou.collaboration.server.pojo.FetchPadDTO;
import com.enihsyou.collaboration.server.pojo.LockPadDTO;
import com.enihsyou.collaboration.server.service.WebsocketService;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

/** 处理前后端在文档和🔒更新上的交互 */
@Controller
public class WebsocketController {

    private final WebsocketService websocketService;

    private final SimpMessageSendingOperations template;

    public WebsocketController(final WebsocketService websocketService, final SimpMessageSendingOperations template) {
        this.websocketService = websocketService;
        this.template = template;
    }

    /** 刷新获取最新状态 */
    @MessageMapping("pad.fetch")
    public void fetchPadStatus(@RequestBody FetchPadDTO fetchPadDTO) {
        template.convertAndSend("pad/" + 1, 1);
    }

    /** 申请🔒 */
    @MessageMapping("pad.lock.acquire")
    public void acquirePadLock(@RequestBody LockPadDTO lockPadDTO) {

    }

    /** 释放🔒 */
    @MessageMapping("pad.lock.release")
    public void releasePadLock(@RequestBody LockPadDTO lockPadDTO) {

    }

    @MessageExceptionHandler
    public void handleException(Throwable exception) {

    }
}

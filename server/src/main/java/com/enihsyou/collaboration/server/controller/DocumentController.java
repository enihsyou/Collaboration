package com.enihsyou.collaboration.server.controller;

import com.enihsyou.collaboration.server.service.DiffService;
import com.enihsyou.collaboration.server.service.DocumentService;
import com.enihsyou.collaboration.server.service.WebsocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/** 处理前后端在文档和🔒更新上的交互 */
@RestController
public class DocumentController {

    private final DocumentService documentService;

    private final WebsocketService websocketService;

    private final DiffService diffService;

    @Autowired
    public DocumentController(
        final DocumentService documentService, final WebsocketService websocketService, final DiffService diffService) {
        this.documentService = documentService;
        this.websocketService = websocketService;
        this.diffService = diffService;
    }
}

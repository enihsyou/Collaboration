package com.enihsyou.collaboration.server.service.impl;

import com.enihsyou.collaboration.server.service.DiffService;
import com.enihsyou.collaboration.server.service.WebsocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
public class WebsocketServiceImpl implements WebsocketService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebsocketService.class);

    private final SimpMessageSendingOperations template;

    private final DiffService diffService;

}

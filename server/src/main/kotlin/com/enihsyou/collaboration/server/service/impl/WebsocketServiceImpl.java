package com.enihsyou.collaboration.server.service.impl;

import com.enihsyou.collaboration.server.domain.CoIndividual;
import com.enihsyou.collaboration.server.domain.CoLock;
import com.enihsyou.collaboration.server.domain.CoPad;
import com.enihsyou.collaboration.server.pojo.FetchPadDTO;
import com.enihsyou.collaboration.server.pojo.LockAcquireDTO;
import com.enihsyou.collaboration.server.pojo.LockReleaseDTO;
import com.enihsyou.collaboration.server.service.DiffService;
import com.enihsyou.collaboration.server.service.WebsocketService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
public class WebsocketServiceImpl implements WebsocketService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebsocketService.class);

    private final SimpMessageSendingOperations template;

    private final DiffService diffService;

    public WebsocketServiceImpl(
        final SimpMessageSendingOperations template,
        final DiffService diffService
    ) {
        this.template = template;
        this.diffService = diffService;
    }

    @NotNull
    @Override
    public CoPad fetchStatus(
        @NotNull final FetchPadDTO fetchPadDTO
    ) {
        return null;
    }

    @NotNull
    @Override
    public CoLock acquireLock(
        @NotNull final LockAcquireDTO lockAcquireDTO, @NotNull final CoIndividual account
    ) {
        return null;
    }

    @NotNull
    @Override
    public CoPad releaseLock(
        @NotNull final LockReleaseDTO lockReleaseDTO, @NotNull final CoIndividual account
    ) {
        return null;
    }
}

package com.enihsyou.collaboration.server.service.impl;

import com.enihsyou.collaboration.server.pojo.DiffResult;
import com.enihsyou.collaboration.server.service.DiffService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DiffServiceImpl implements DiffService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DiffService.class);

    @NotNull
    @Override
    public DiffResult compare() {
        return null;
    }
}

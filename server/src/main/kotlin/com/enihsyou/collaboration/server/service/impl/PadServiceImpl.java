package com.enihsyou.collaboration.server.service.impl;

import com.enihsyou.collaboration.server.domain.CoIndividual;
import com.enihsyou.collaboration.server.domain.CoInviteLink;
import com.enihsyou.collaboration.server.domain.CoPad;
import com.enihsyou.collaboration.server.domain.CoPadInstant;
import com.enihsyou.collaboration.server.pojo.PadSaveDTO;
import com.enihsyou.collaboration.server.service.PadService;
import com.enihsyou.collaboration.server.util.ShareLevel;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PadServiceImpl implements PadService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PadService.class);

    @NotNull
    @Override
    public CoPadInstant saveInstant(
        final long padId, @NotNull final PadSaveDTO padSaveDTO
    ) {
        return null;
    }

    @NotNull
    @Override
    public CoPad revertInstant(final long padId, final long revision) {
        return null;
    }

    @NotNull
    @Override
    public CoInviteLink sharePad(
        final long padId, @NotNull final ShareLevel shareLevel
    ) {
        return null;
    }

    @NotNull
    @Override
    public CoPad joinPad(@NotNull final String token, @NotNull final CoIndividual account) {
        return null;
    }
}

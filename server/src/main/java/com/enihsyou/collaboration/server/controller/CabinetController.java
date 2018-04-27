package com.enihsyou.collaboration.server.controller;

import com.enihsyou.collaboration.server.service.CabinetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CabinetController {

    private final CabinetService cabinetServicel;

    @Autowired
    public CabinetController(final CabinetService cabinetServicel) {this.cabinetServicel = cabinetServicel;}
}


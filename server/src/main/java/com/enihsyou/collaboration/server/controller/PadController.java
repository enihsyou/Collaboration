package com.enihsyou.collaboration.server.controller;

import com.enihsyou.collaboration.server.service.PadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PadController{

    private final PadService padService;

    @Autowired
    public PadController(final PadService padService) {this.padService = padService;}
}


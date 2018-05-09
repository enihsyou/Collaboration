package com.enihsyou.collaboration.server.controller;

import com.enihsyou.collaboration.server.service.PadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("account/cabinet/pad")
public class PadController {

    private final PadService padService;

    @Autowired
    public PadController(final PadService padService) {this.padService = padService;}


    @PostMapping
    public void createPad() {}

    @PostMapping("{padId}/share/link")
    public void sharePad(@PathVariable String padId) {}

    public void startEdit() {}

}



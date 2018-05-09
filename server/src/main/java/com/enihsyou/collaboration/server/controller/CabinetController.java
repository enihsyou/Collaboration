package com.enihsyou.collaboration.server.controller;

import com.enihsyou.collaboration.server.service.CabinetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("account/cabinets")
public class CabinetController {

    private final CabinetService cabinetServicel;

    @Autowired
    public CabinetController(final CabinetService cabinetService) {this.cabinetServicel = cabinetService;}

    @GetMapping
    public void detailCabinet(@PathVariable Long cabinetId,
                              @RequestParam(required = false, defaultValue = "brief") String level) {

    }

    @PostMapping
    public void createCabinet() {

    }

}


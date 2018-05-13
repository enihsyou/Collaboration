package com.enihsyou.collaboration.server.controller;

import com.enihsyou.collaboration.server.pojo.RestResponse;
import com.enihsyou.collaboration.server.service.CabinetService;
import com.enihsyou.collaboration.server.service.PadService;
import com.enihsyou.collaboration.server.util.DetailLevel;
import com.enihsyou.collaboration.server.util.ShareLevel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/** 用户文稿集合管理的相关控制器 */
@RestController
@RequestMapping("cabinet")
public class CabinetController {

    private final CabinetService cabinetService;

    private final PadService padService;

    public CabinetController(final CabinetService cabinetService, final PadService padService) {
        this.cabinetService = cabinetService;
        this.padService = padService;
    }

    /** 获取用户文件柜的最新状态 */
    @GetMapping
    public RestResponse detailCabinet(@RequestParam(required = false,
        defaultValue = DetailLevel.LEVEL_BRIEF) String level) {
        DetailLevel detailLevel = DetailLevel.parseLevel(level);
        return RestResponse.ok(null);

    }

    /** 用户创建一篇新文稿 */
    @PostMapping("pads")
    public RestResponse createPad() {
        return RestResponse.ok(null);
    }

    /**
     * 用户只读预览一篇文稿
     *
     * @param padId 想要预览的文稿id号
     */
    @GetMapping("pads/{padId}")
    public RestResponse previewPad(@PathVariable long padId) {
        return RestResponse.ok(null);

    }

    /**
     * 用户开始编辑一篇文稿
     *
     * @param padId 想要进行编辑的文稿id号
     */
    @PostMapping("pads/{padId}/edit")
    public RestResponse editPad(@PathVariable long padId) {
        return RestResponse.ok(null);

    }

    /**
     * 用户分享协同文稿
     *
     * @param padId 需要分享的文稿id号
     * @param level 创建出来的分享链接的分享等级
     */
    @PostMapping("share")
    public RestResponse sharePad(@RequestParam long padId, @RequestParam String level) {
        ShareLevel shareLevel = ShareLevel.parseLevel(level);
        return RestResponse.ok(null);

    }

    /**
     * 用户加入协同文稿
     *
     * @param token 加入文稿所需的邀请码
     */
    @PostMapping("join")
    public RestResponse joinPad(@RequestParam String token) {
        return RestResponse.ok(null);

    }
}


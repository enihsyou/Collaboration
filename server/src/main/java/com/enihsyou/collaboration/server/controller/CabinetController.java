package com.enihsyou.collaboration.server.controller;

import com.enihsyou.collaboration.server.domain.*;
import com.enihsyou.collaboration.server.pojo.PadCreateDTO;
import com.enihsyou.collaboration.server.pojo.PadSaveDTO;
import com.enihsyou.collaboration.server.pojo.RestResponse;
import com.enihsyou.collaboration.server.service.CabinetService;
import com.enihsyou.collaboration.server.service.PadService;
import com.enihsyou.collaboration.server.util.DetailLevel;
import com.enihsyou.collaboration.server.util.PermissionUtils;
import com.enihsyou.collaboration.server.util.ShareLevel;
import org.springframework.web.bind.annotation.*;


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
    public RestResponse detailCabinet(
        @RequestParam(required = false, defaultValue = DetailLevel.LEVEL_BRIEF) String level
    ) {
        final CoIndividual account = PermissionUtils.loggedAccount();

        DetailLevel detailLevel = DetailLevel.parseLevel(level);

        CoCabinet cabinet = cabinetService.fetchCabinet(account);

        return RestResponse.ok(ExtensionsKt.toDetailVO(cabinet, detailLevel));
    }

    /** 用户创建一篇新文稿 */
    @PostMapping("pads")
    public RestResponse createPad(@RequestBody PadCreateDTO padCreateDTO) {
        final CoIndividual account = PermissionUtils.loggedAccount();

        final CoPad pad = cabinetService.createPad(padCreateDTO, account);

        return RestResponse.ok(ExtensionsKt.toCreateVO(pad));
    }

    /**
     * 用户只读预览一篇自己文件柜里的文稿
     * 内容包含历史记录的引用，和当前文档中的锁的情况
     *
     * @param padId 想要预览的文稿id号
     */
    @GetMapping("pads/{padId}")
    public RestResponse previewPad(@PathVariable long padId) {
        final CoIndividual account = PermissionUtils.loggedAccount();

        final CoPad pad = cabinetService.fetchPad(padId, account);

        return RestResponse.ok(ExtensionsKt.toDetailVO(pad));
    }

    // /**
    //  * 用户开始编辑一篇文稿
    //  *
    //  * @param padId 想要进行编辑的文稿id号
    //  */
    // @PostMapping("pads/{padId}/edit")
    // public RestResponse editPad(@PathVariable long padId) {
    //
    //
    //     return RestResponse.ok(null);
    //
    // }

    /**
     * 获取用户文档历史状态列表
     *
     * @param padId 对应的文档id
     */
    @GetMapping("pads/{padId}/revisions")
    public RestResponse fetchPadRevisions(@PathVariable long padId) {
        final CoIndividual account = PermissionUtils.loggedAccount();

        final CoPad pad = cabinetService.fetchPad(padId, account);

        return RestResponse.ok(ExtensionsKt.toRevisionDetailVO(pad));
    }

    /**
     * 用户手动保存一篇文稿的当前状态
     *
     * @param padId      想要进行保存历史版本的文稿id号
     * @param padSaveDTO 保存时需要用户提供的信息
     */
    @PutMapping("pads/{padId}/revisions")
    public RestResponse savePadInstant(@PathVariable long padId, @RequestBody PadSaveDTO padSaveDTO) {
        final CoIndividual account = PermissionUtils.loggedAccount();

        final CoPadInstant instant = padService.saveInstant(padId, padSaveDTO, account);

        return RestResponse.ok(ExtensionsKt.toInstantSavedVO(instant));
    }

    /**
     * 拥有该文档的用户，将文档回滚到之前的状态
     * 回滚后，在之后的历史状态将不存在
     *
     * @param padId    想要进行回滚的文稿id号
     * @param revision 目标的回滚版本号
     */
    @PostMapping("pads/{padId}/revisions")
    public RestResponse revertPadInstant(@PathVariable long padId, @RequestParam long revision) {
        final CoIndividual account = PermissionUtils.loggedAccount();

        final CoPad pad = padService.revertInstant(padId, revision, account);

        return RestResponse.ok(ExtensionsKt.toDetailVO(pad));
    }

    /**
     * 用户分享协同文稿
     *
     * @param padId 需要分享的文稿id号
     * @param level 创建出来的分享链接的分享等级
     */
    @PostMapping("pads/{padId}/share")
    public RestResponse sharePad(@PathVariable long padId, @RequestParam String level) {
        final CoIndividual account = PermissionUtils.loggedAccount();

        ShareLevel shareLevel = ShareLevel.parseLevel(level);

        final CoInviteLink inviteLink = padService.sharePad(padId, shareLevel, account);

        return RestResponse.ok(ExtensionsKt.toCreateVO(inviteLink));
    }

    /**
     * 用户加入协同文稿
     *
     * @param token 加入文稿所需的邀请码
     */
    @PostMapping("join")
    public RestResponse joinPad(@RequestParam String token) {
        final CoIndividual account = PermissionUtils.loggedAccount();

        final CoPad pad = padService.joinPad(token, account);

        return RestResponse.ok(ExtensionsKt.toDetailVO(pad));
    }
}


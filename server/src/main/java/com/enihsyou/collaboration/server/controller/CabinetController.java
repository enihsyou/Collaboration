package com.enihsyou.collaboration.server.controller;

import com.enihsyou.collaboration.server.domain.CoCabinet;
import com.enihsyou.collaboration.server.domain.CoIndividual;
import com.enihsyou.collaboration.server.domain.CoInviteLink;
import com.enihsyou.collaboration.server.domain.CoPad;
import com.enihsyou.collaboration.server.domain.CoPadInstant;
import com.enihsyou.collaboration.server.domain.ExtensionsKt;
import com.enihsyou.collaboration.server.pojo.PadCreateDTO;
import com.enihsyou.collaboration.server.pojo.PadSaveDTO;
import com.enihsyou.collaboration.server.pojo.PadUpdateDTO;
import com.enihsyou.collaboration.server.pojo.RestResponse;
import com.enihsyou.collaboration.server.service.CabinetService;
import com.enihsyou.collaboration.server.service.PadService;
import com.enihsyou.collaboration.server.util.DetailLevel;
import com.enihsyou.collaboration.server.util.PermissionUtils;
import com.enihsyou.collaboration.server.util.ShareLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/** 用户文稿集合管理的相关控制器 */
@RestController
@RequestMapping("cabinet")
public class CabinetController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CabinetController.class);

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
        LOGGER.debug("刷新文件柜状态 [{}] detail level: {}", PermissionUtils.currentUsername(), level);

        final CoIndividual account = PermissionUtils.loggedAccount();

        DetailLevel detailLevel = DetailLevel.parseLevel(level);

        CoCabinet cabinet = cabinetService.fetchCabinet(account);

        return RestResponse.ok(ExtensionsKt.toDetailVO(cabinet, detailLevel));
    }

    /** 用户创建一篇新文稿 */
    @PostMapping("pads")
    public RestResponse createPad(@RequestBody PadCreateDTO padCreateDTO) {
        LOGGER.debug("创建新文稿 [{}] title: {}", PermissionUtils.currentUsername(), padCreateDTO.getTitle());

        final CoIndividual account = PermissionUtils.loggedAccount();

        final CoPad pad = cabinetService.createPad(padCreateDTO, account);

        return RestResponse.ok(ExtensionsKt.toCreateVO(pad));
    }

    /**
     * 用户修改文稿的信息
     * 需要管理权限
     *
     * @param padId 修改的目标id号
     */
    @PutMapping("pads/{padId}")
    public RestResponse updatePad(@PathVariable long padId, @RequestBody PadUpdateDTO padUpdateDTO) {
        final String username = PermissionUtils.currentUsername();
        LOGGER.debug("更新文稿信息 [{}] #{} new title: {}",
            username, padId, padUpdateDTO.getTitle());

        PermissionUtils.checkOwnership(padId, username);

        final CoPad pad = cabinetService.updatePad(padId, padUpdateDTO);

        return RestResponse.ok(ExtensionsKt.toCreateVO(pad));
    }

    /**
     * 用户只读预览一篇自己文件柜里的文稿
     * 内容包含历史记录的引用，和当前文档中的锁的情况
     * 需要编辑权限
     *
     * @param padId 想要预览的文稿id号
     */
    @GetMapping("pads/{padId}")
    public RestResponse previewPad(@PathVariable long padId) {
        final String username = PermissionUtils.currentUsername();
        LOGGER.debug("预览文稿 [{}] #{}", username, padId);

        PermissionUtils.checkCoopship(padId, username);

        final CoPad pad = cabinetService.fetchPad(padId);

        return RestResponse.ok(ExtensionsKt.toDetailVO(pad));
    }

    /**
     * 获取用户文档历史状态的详情列表
     * 需要编辑权限
     *
     * @param padId 对应的文档id
     */
    @GetMapping("pads/{padId}/revisions")
    public RestResponse fetchPadRevisions(@PathVariable long padId) {
        final String username = PermissionUtils.currentUsername();
        LOGGER.debug("获取文稿历史状态 [{}] #{}", username, padId);

        PermissionUtils.checkCoopship(padId, username);

        final CoPad pad = cabinetService.fetchPad(padId);

        return RestResponse.ok(ExtensionsKt.toRevisionDetailVO(pad));
    }

    /**
     * 用户手动保存一篇文稿的当前状态
     * 需要编辑权限
     *
     * @param padId      想要进行保存历史版本的文稿id号
     * @param padSaveDTO 保存时需要用户提供的信息
     */
    @PutMapping("pads/{padId}/revisions")
    public RestResponse savePadInstant(@PathVariable long padId, @RequestBody PadSaveDTO padSaveDTO) {
        final String username = PermissionUtils.currentUsername();
        LOGGER.debug("保存文稿历史状态 [{}] pad: #{} revision: #{} tag: {}",
            username, padId, padSaveDTO.getTag());

        PermissionUtils.checkCoopship(padId, username);

        final CoPadInstant instant = padService.saveInstant(padId, padSaveDTO);

        return RestResponse.ok(ExtensionsKt.toInstantSavedVO(instant));
    }

    /**
     * 拥有该文档的用户，将文档回滚到之前的状态
     * 回滚后，在之后的历史状态将不存在
     * 需要管理权限
     *
     * @param padId    想要进行回滚的文稿id号
     * @param revision 目标的回滚版本号
     */
    @PostMapping("pads/{padId}/revisions")
    public RestResponse revertPadInstant(@PathVariable long padId, @RequestParam long revision) {
        final String username = PermissionUtils.currentUsername();
        LOGGER.debug("回滚文稿状态 [{}] pad: #{} revision: #{}", username, padId, revision);

        PermissionUtils.checkOwnership(padId, username);

        final CoPad pad = padService.revertInstant(padId, revision);

        return RestResponse.ok(ExtensionsKt.toDetailVO(pad));
    }

    /**
     * 用户分享协同文稿
     * 需要管理权限
     *
     * @param padId 需要分享的文稿id号
     * @param level 创建出来的分享链接的分享等级
     */
    @PostMapping("pads/{padId}/share")
    public RestResponse sharePad(@PathVariable long padId, @RequestParam String level) {
        final String username = PermissionUtils.currentUsername();
        LOGGER.debug("分享文稿 [{}] #{} share level: {}", username, padId, level);

        PermissionUtils.checkOwnership(padId, username);

        ShareLevel shareLevel = ShareLevel.parseLevel(level);

        final CoInviteLink inviteLink = padService.sharePad(padId, shareLevel);

        return RestResponse.ok(ExtensionsKt.toCreateVO(inviteLink));
    }

    /**
     * 用户加入协同文稿
     *
     * @param token 加入文稿所需的邀请码
     */
    @PostMapping("join")
    public RestResponse joinPad(@RequestParam String token) {
        LOGGER.debug("加入文稿 [{}] token: {}", PermissionUtils.currentUsername(), token);

        final CoIndividual account = PermissionUtils.loggedAccount();

        final CoPad pad = padService.joinPad(token, account);

        return RestResponse.ok(ExtensionsKt.toDetailVO(pad));
    }
}

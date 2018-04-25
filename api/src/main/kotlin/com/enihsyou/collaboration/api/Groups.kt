package com.enihsyou.collaboration.api

import java.time.LocalDateTime

typealias EmailAddress = String
typealias Username = String
typealias Password = String
typealias InviteToken = String
typealias UserIdentifier = Long

/**代表每个用户的具体信息*/
class CoIndividual(

    /**用户登录名*/
    val username: Username,

    /**用户密码，禁止保存明文*/
    val password: Password,

    /**用户最后登录的时间*/
    val lastLoginTime: LocalDateTime,

    /**用户注册的时间*/
    val createdTime: LocalDateTime
)

enum class CoLinkStatus {

    /**用户能查看文稿*/
    CAN_VIEW,

    /**用户能参与文稿的编辑*/
    CAN_EDIT,

    /**授予用户的权限被收回*/
    REVOKED
}

interface ICoGroup {
    fun listGroups()
    fun createGroup()
    fun deleteGroup()
    fun listIndividuals()
    fun createIndividual()
}

/**每个用户都有的 文件柜
 * 用来存放用户拥有的所有文档，包括创建的和被邀请合作的*/
class CoCabinet(owner: CoIndividual) {

    /**标注这个文件柜属于哪个用户的*/
    val belongTo: CoIndividual = owner

    /**文件柜里面存有的文档集合，包括被邀请加入的*/
    val pads: MutableList<CoPadControlBlock> = mutableListOf()

    /**文件柜的创建时间*/
    val createdTime: LocalDateTime = LocalDateTime.now()
}

interface ICoCabinet {
    fun listCabinets()
    fun createCabinet()
    fun deleteCabinet()

    fun listPads()
    fun createCabinetPad()
    fun deletePad()
}

/**独立的一篇文稿的瞬时状态
 * 只有在文稿不存在锁的时候　才能执行保存状态的操作
 * 所以这个对象表示的是，文稿的历史状态之一
 * 不同用户可能会同时操作同一篇文稿*/
class CoInstantPad(

    /**为这个瞬时状态添加的标记*/
    val tag: String? = null,

    /**文稿标题*/
    val title: String = "",

    /**文稿主体*/
    val body: String = "",

    /**文章中每个用户的贡献区间*/
    val contributes: MutableMap<CoIndividual, MutableList<CoBlame>> = mutableMapOf(),

    /**文稿的创建时间*/
    val createdTime: LocalDateTime = LocalDateTime.now()
)

/**独立的一篇文稿的当前状态
 * 包含文稿的所有历史状态
 * 不同用户可能会同时操作同一篇文稿*/
class CoPad(owner: CoIndividual) {

    /**创建本文稿的用户，具有对文稿管理的最高权限*/
    val belongTo: CoIndividual = owner

    /**文稿的每个历史状态*/
    val instants: MutableList<CoInstantPad> = mutableListOf()

    /**文稿标题*/
    val title: String get() = instants.lastOrNull()?.title ?: ""

    /**当前文章中的🔒，每一个都是互相不重叠的区间范围
     * 如果文档中存在未释放的🔒，则不能保存历史状态*/
    val locks: MutableList<CoLock> = mutableListOf()
}

/**代表文件柜[CoCabinet]和文稿[CoPad]之间链接的相关信息
 * 实际作用是将用户的文件柜和独立的文稿连接起来，
 * 这篇文稿可能是自己创建的，也可能是别人邀请用户加入的*/
class CoPadControlBlock(

    /**链接的一端：文件柜*/
    val cabinet: CoCabinet,

    /**链接的另一端：文稿*/
    val pad: CoPad,

    /**链接状态*/
    val status: CoLinkStatus,

    /**链接的创建时间*/
    val createdTime: LocalDateTime
)

open class CoRange(
    val from: Int,
    val to: Int
)

/**一段被🔒锁定的文字范围*/
class CoLock(val belongTo: CoIndividual, val range: IntRange) : CoRange(range.first, range.last)

/**一段属于用户贡献的位置范围*/
class CoBlame(val belongTo: CoIndividual, val range: IntRange) : CoRange(range.first, range.last)

/**邀请链接
 * 通过[InviteLink.inviter]邀请[InviteLink.invitee]加入文档[InviteLink.pad]，并授予[InviteLink.permission]权限
 * 如果多次邀请同一个被邀请者，应该返回同一个邀请码*/
class InviteLink(inviteTo: CoPad, invitee_: EmailAddress, permission_: CoLinkStatus) {

    /**发给被邀请用户的授权令牌，用户使用这个串确认邀请*/
    val token: InviteToken = "" // todo use Util to generate SHA1 token

    /**发起邀请的人*/
    val inviter: CoIndividual get() = pad.belongTo

    /**被邀请的人*/
    val invitee: EmailAddress = invitee_

    /**被邀请者加入的文稿[CoPad]*/
    val pad: CoPad = inviteTo

    /**被邀请者被授予的权限*/
    val permission: CoLinkStatus = permission_

    /**这个邀请链接的创建时间*/
    val createdTime: LocalDateTime = LocalDateTime.now()

    /**这个邀请链接过期的时间，重复邀请会延长寿命*/
    val expiredTime: LocalDateTime = LocalDateTime.now().plusDays(1)
}

fun main(args: Array<String>) {
    1..3
}

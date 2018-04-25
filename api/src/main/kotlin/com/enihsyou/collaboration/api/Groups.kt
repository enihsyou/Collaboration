package com.enihsyou.collaboration.api

import java.time.LocalDateTime

class CoGroup

class CoIndividual

enum class CoUserRole {
    CAN_EDIT, CAN_VIEW
}

enum class CoLinkStatus {
    /**用户能参与文稿的编辑*/
    CAN_EDIT,
    /**用户能查看文稿*/
    CAN_VIEW,
    /**授予用户的权限被收回*/
    REVOKED
}

interface ICoGroup {
    fun listGroups()
    fun createGroup()
    fun deleteGroup()
    fun listIndivisuals()
    fun createIndivisual()
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

/**独立的一篇文稿
 * 不同用户可能会同时操作同一篇文稿*/
class CoPad(owner: CoIndividual, title_: String = "") {

    /**创建本文稿的用户，具有对文稿管理的最高权限*/
    val belongTo: CoIndividual = owner

    /**文稿标题*/
    var title: String = title_

    /**文稿主体*/
    var body: String = ""

    /**文章中的🔒，每一个都是互相不重叠的区间范围*/
    val locks: MutableList<CoLock> = mutableListOf()

    /**文章中每个用户的贡献区间*/
    val contributes: MutableMap<CoIndividual, MutableList<CoBlame>> = mutableMapOf()

    /**文稿的创建时间*/
    val createdTime: LocalDateTime = LocalDateTime.now()
}

/**具有链接文件柜[CoCabinet]和文稿[CoPad]的相关信息*/
class CoPadControlBlock(
    val cabinet: CoCabinet,
    val pad: CoPad,
    val status: CoLinkStatus,
    val createdTime: LocalDateTime
)

/**构成文章的节点*/
sealed class CoDocumentNode

/**由单独的文字构成的节点，内部没有其他组件*/
class CoTextNode : CoDocumentNode()

/**由🔒构成的节点，内部包含一段文字*/
class CoPadlockNode : CoDocumentNode()

open class CoRange(
    val from: Int,
    val to: Int
)

/**一段被🔒锁定的文字范围*/
class CoLock(val belongTo: CoIndividual, range: IntRange) : CoRange(range.first, range.last)

/**一段属于用户贡献的位置范围*/
class CoBlame(val belongTo: CoIndividual, range: IntRange) : CoRange(range.first, range.last)

fun main(args: Array<String>) {
    1..3
}

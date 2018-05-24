package com.enihsyou.collaboration.server.service.impl

import com.enihsyou.collaboration.server.domain.CoBlame
import com.enihsyou.collaboration.server.domain.CoIndividual
import com.enihsyou.collaboration.server.domain.CoLock
import com.enihsyou.collaboration.server.domain.CoPad
import com.enihsyou.collaboration.server.domain.CoPadInstant
import com.enihsyou.collaboration.server.pojo.RangeCollapsedException
import com.enihsyou.collaboration.server.service.DocumentService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import kotlin.math.max
import kotlin.math.min

@Service
class DocumentServiceImpl : DocumentService {

    override fun acquire(pad: CoPad, requestRange: IntRange, operator: CoIndividual): CoLock {
        /*检查范围是否有重叠*/
        if (!requestRange.isEmpty()) {
            synchronized(this) {
                pad.locks.forEach {
                    val lock_range = it.range
                    if (lock_range overlappedWith requestRange)
                        throw RangeCollapsedException(lock_range, requestRange)
                }
            }
        }

        val lock = CoLock()
            .setLocker(operator)
            .setPad(pad)
            .setRange(requestRange)

        synchronized(this) {
            pad.addLock(lock)
        }
        return lock
    }

    override fun release(lock: CoLock, replacement: String): CoPad {
        val pad = lock.pad


        /*对文稿的锁和贡献进行更新*/
        val modifiedRange = lock.range
        val shiftLength = replacement.length - modifiedRange.length()

        /**
         * 对锁需要的操作是：
         *
         * - 移除已经使用的 和已经失效的锁。
         *
         * - 移动其他锁
         * 如果这个锁在提交的锁的前面，不进行操作
         * 如果和这个锁处于相同位置，不进行操作
         * 如果在锁的后面，进行平移操作
         * */
        val lockToBeRemoved = mutableListOf<CoLock>()
        pad.locks.forEach {
            /*移除使用掉的锁和已经失效的锁*/
            if (it === lock || it.isExpired) {
                lockToBeRemoved += it
                return@forEach
            }

            /*移动锁*/
            if (it.range isBehind modifiedRange && it.createdTime > lock.createdTime) {
                it.range += shiftLength
            }
        }
        pad.locks -= lockToBeRemoved

        /**
         * 对贡献需要操作的是：
         *
         * - 如果这个贡献区间在锁定的前面，不进行操作
         *
         * - 如果在后面，增加或减少变动的长度，也就是将区间平移一段距离
         * 这段距离由 (锁定之前的长度 - [替换字符串][replacement]]的长度) 计算得到
         *
         * - 如果在中间，切分成两段
         * */
        val blameToBeAdded = mutableListOf<CoBlame>()
        val blameToBeRemoved = mutableListOf<CoBlame>()
        pad.contributes.forEach {
            if (it.range isBehind modifiedRange && it.createdTime > lock.createdTime) {
                it.range += shiftLength
            } else if (modifiedRange overlappedWith it.range) {
                val start1 = it.left
                val end1 = modifiedRange.start

                val start2 = modifiedRange.endInclusive + shiftLength
                val end2 = it.right + shiftLength

                /*处理锁定区间处于一个已有的贡献区间之内的特殊情况，这时候会拆分成两块新的贡献，这里添加新生成的部分*/
                if (start1 < end1 && start2 < end2)
                    blameToBeAdded += CoBlame()
                        .setRange(start2..end2)
                        .setContributor(it.contributor)
                        .setPad(it.pad)
                        .setCreatedTime(it.createdTime)

                /*处理左值右值和覆盖*/
                when {
                    start1 < end1 -> it.range = start1..end1
                    start2 < end2 -> it.range = start2..end2
                    else          -> blameToBeRemoved += it
                }
            }
        }
        /*添加新增的贡献*/
        blameToBeAdded += CoBlame.from(lock)
            .recalculateUsingReplacement(replacement)
        pad.contributes -= blameToBeRemoved
        pad.contributes += blameToBeAdded

        pad.body = pad.body.replaceRange(modifiedRange, replacement)

        val instant = CoPadInstant()
            .setBelongTo(pad)
            .setCreatedBy(lock.locker)
            .setBody(pad.body)
            .setTag(null)
            .setContributes(pad.contributes)

        pad.addInstants(instant)

        return pad
    }

    companion object {

        private val LOGGER = LoggerFactory.getLogger(DocumentService::class.java)
    }

    private fun String.replaceRange(range: IntRange, replacement: String): String =
        replaceRange(range.start, range.endInclusive, replacement)

    private infix fun IntProgression.overlappedWith(other: IntProgression): Boolean =
        max(first, other.first) < min(last, other.last)

    private infix fun IntProgression.isBehind(other: IntProgression): Boolean =
        last <= other.first

    private fun IntProgression.length() = last - first

    private operator fun IntProgression.plus(number: Int): IntRange {
        return (first + number)..(last + number)
    }
}

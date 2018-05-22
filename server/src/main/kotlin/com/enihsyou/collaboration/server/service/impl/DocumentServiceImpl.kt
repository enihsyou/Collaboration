package com.enihsyou.collaboration.server.service.impl

import com.enihsyou.collaboration.server.domain.CoBlame
import com.enihsyou.collaboration.server.domain.CoIndividual
import com.enihsyou.collaboration.server.domain.CoLock
import com.enihsyou.collaboration.server.domain.CoPad
import com.enihsyou.collaboration.server.pojo.RangeCollapsedException
import com.enihsyou.collaboration.server.service.DocumentService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import kotlin.math.max
import kotlin.math.min

@Service
class DocumentServiceImpl : DocumentService {

    override fun lock(pad: CoPad, requestRange: IntRange, operator: CoIndividual): CoLock {
        /*检查范围是否有重叠*/
        if (requestRange.length() != 0) {
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

        val blame = CoBlame.from(lock)
            .extendLength(replacement.length)

        /*添加贡献记录*/
        pad.addContribute(blame)
        /*并移除锁*/
        pad.removeLock(lock)

        /*接下来对文稿的锁和贡献区间进行更新*/
        val modified_range = lock.range

        /**
         * 对贡献需要操作的是：
         *
         * - 如果这个贡献区间在锁定的前面，不进行操作
         *
         * - 如果在后面，增加或减少变动的长度，也就是将区间平移一段距离
         * 这段距离由 (锁定之前的长度 - [replacement] 替换字符串的长度) 计算得到
         *
         * */
        pad.contributes.forEach {
            val blame_range = it.range
            if (blame_range isBehind modified_range && it.createdTime > lock.createdTime) {
                it.range = blame_range + (replacement.length - modified_range.length())
            }
        }
        pad.locks.forEach {
            val lock_range = it.range
            if (lock_range isBehind modified_range && it.createdTime > lock.createdTime) {
                it.range = lock_range + (replacement.length - modified_range.length())
            }
        }

        val modifiedBody = pad.body.replaceRange(modified_range, replacement)
        pad.body = modifiedBody

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

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
                blameToBeRemoved += it

                val start1 = it.left
                val end1 = modifiedRange.start
                if (start1 < end1)
                    blameToBeAdded += it.setRange(start1..end1)

                val start2 = modifiedRange.endInclusive + shiftLength
                val end2 = it.right + shiftLength
                if (start2 < end2)
                    blameToBeAdded += it.setRange(start2..end2)
            }
        }
        blameToBeAdded += CoBlame.from(lock)
            .recalculateUsingReplacement(replacement)
        pad.contributes -= blameToBeRemoved
        pad.contributes += blameToBeAdded

        val modifiedBody = pad.body.replaceRange(modifiedRange, replacement)
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

    private infix fun IntProgression.isInside(other: IntProgression): Boolean =
        last <= other.first

    private fun IntProgression.length() = last - first

    private operator fun IntProgression.plus(number: Int): IntRange {
        return (first + number)..(last + number)
    }
}

fun main(args: Array<String>) {
    val list = mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9)
    val toBeRemoved = mutableListOf<Int>()
    list.forEach {
        if (it % 2 == 0) {
            toBeRemoved += it
            return@forEach
        }
        println(it)
    }
    list -= toBeRemoved
    println(list)
}

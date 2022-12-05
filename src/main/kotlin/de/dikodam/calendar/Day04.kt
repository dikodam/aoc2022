package de.dikodam.calendar

import de.dikodam.AbstractDay
import de.dikodam.containsRange
import de.dikodam.executeTasks
import kotlin.time.ExperimentalTime

@ExperimentalTime
fun main() {
    Day04().executeTasks()
}

class Day04 : AbstractDay() {

    private val inputRanges = readInputLines()
        .map { parseRanges(it) }

    override fun task1(): String {
        return inputRanges
            .count { (r1, r2) -> r1.containsRange(r2) || r2.containsRange(r1) }
            .toString()
    }

    private fun parseRanges(line: String): Pair<IntRange, IntRange> {
        val (firstRange, secondRange) = line.split(",")
            .map { rangeString ->
                val (from, to) = rangeString.split("-").map { it.toInt() }
                from..to
            }
        return firstRange to secondRange
    }

    override fun task2(): String {
        return inputRanges
            .count { (r1, r2) -> r1.intersect(r2.toSet()).isNotEmpty() }
            .toString()
    }
}

package de.dikodam.calendar

import de.dikodam.AbstractDay
import de.dikodam.executeTasks
import kotlin.time.ExperimentalTime

@ExperimentalTime
fun main() {
    Day06().executeTasks()
}

class Day06 : AbstractDay() {

    private val input = readInputRaw()

    override fun task1(): String {
        return findStartOfPacketMarkerPosition(input).toString()
    }

    override fun task2(): String {
        return findStartOfMessageMarkerPosition(input).toString()
    }
}

internal fun findStartOfPacketMarkerPosition(line: String): Int = charsToConsumeForFirstXUniqueChars(line, 4)

internal fun findStartOfMessageMarkerPosition(line: String): Int = charsToConsumeForFirstXUniqueChars(line, 14)

internal fun charsToConsumeForFirstXUniqueChars(line: String, uniqueCharCount: Int): Int {
    val indexOfFirstMarker = line.windowed(size = uniqueCharCount, partialWindows = false)
        .indexOfFirst { packet -> packet.asIterable().distinct().count() == uniqueCharCount }
    return indexOfFirstMarker + uniqueCharCount
}



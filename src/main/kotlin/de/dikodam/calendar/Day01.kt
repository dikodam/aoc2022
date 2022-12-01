package de.dikodam.calendar

import de.dikodam.AbstractDay
import de.dikodam.divideAt
import de.dikodam.executeTasks
import kotlin.time.ExperimentalTime

@ExperimentalTime
fun main() {
    Day01().executeTasks()
}

class Day01 : AbstractDay() {

    private val input: List<String> = readInputLines()
    private val caloriesPerElf: List<Int> = input
        .divideAt("")
        .map { list -> list.sumOf { it.toInt() } }
        .sortedDescending()

    override fun task1(): String {
        return caloriesPerElf[0].toString()
    }

    override fun task2(): String {
        return caloriesPerElf.take(3).sum().toString()
    }
}

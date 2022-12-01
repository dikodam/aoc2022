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

    private val input = readInputLines()
    private val caloriesPerElf = input.divideAt("")
        .map { list -> list.sumOf { it.toInt() } }

    override fun task1(): String {
        return caloriesPerElf.max().toString()
    }

    override fun task2(): String {
        return caloriesPerElf.sortedDescending().take(3).sum().toString()
    }
}

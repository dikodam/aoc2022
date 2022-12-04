package de.dikodam.calendar

import de.dikodam.AbstractDay
import de.dikodam.executeTasks
import de.dikodam.splitAfter
import java.lang.StringBuilder
import kotlin.time.ExperimentalTime

@ExperimentalTime
fun main() {
    Day03().executeTasks()
}

class Day03 : AbstractDay() {

    val input = readInputLines()

    override fun task1(): String {
        return input
            .map { line -> line.splitAfter(line.length / 2) }
            .map { (l, r) -> l.toSet().intersect(r.toSet()).take(1)[0] }
            .sumOf { it.priority() }
            .toString()
    }

    private fun Char.priority(): Int =
        when (this) {
            in 'a'..'z' -> this - 'a' + 1
            in 'A'..'Z' -> this - 'A' + 27
            else -> error("invalid input $this")
        }

    override fun task2(): String {
        return input
            .chunked(3)
            .map { (group1, group2, group3) ->
                group1.toSet().intersect(group2.toSet()).intersect(group3.toSet()).take(1)[0]
            }
            .sumOf { it.priority() }
            .toString()
    }

}
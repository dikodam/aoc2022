package de.dikodam.calendar

import de.dikodam.AbstractDay
import de.dikodam.calendar.MatchResult.*
import de.dikodam.calendar.RPS.*
import de.dikodam.executeTasks
import kotlin.time.ExperimentalTime

@ExperimentalTime
fun main() {
    Day02().executeTasks()
}

class Day02 : AbstractDay() {
    val input = readInputLines()

    override fun task1(): String {
        return input
            .map { parseLineToEncounter(it) }
            .map(Encounter::score)
            .sum()
            .toString()
    }

    override fun task2(): String {
        return input
            .map { parseLineToActualStrategy(it) }
            .map(ActualStrategy::score)
            .sum()
            .toString()
    }
}

private fun parseLineToEncounter(line: String): Encounter {
    val (theirString, mineString) = line.split(" ")
    return Encounter(RPS.parse(mineString), RPS.parse(theirString))
}

private fun parseLineToActualStrategy(line: String): ActualStrategy {
    val (theirString, mineString) = line.split(" ")
    val desiredMatchResult = when (mineString) {
        "X" -> LOSS
        "Y" -> DRAW
        "Z" -> WIN
        else -> error("invalid input: $mineString")
    }
    return ActualStrategy(RPS.parse(theirString), desiredMatchResult)
}

private enum class RPS(val score: Int) {
    ROCK(1), PAPER(2), SCISSORS(3);

    companion object {
        fun parse(char: String) =
            when (char) {
                "A" -> ROCK
                "B" -> PAPER
                "C" -> SCISSORS
                "X" -> ROCK
                "Y" -> PAPER
                "Z" -> SCISSORS
                else -> error("invalid input $char")
            }
    }
}

private enum class MatchResult(val score: Int) {
    WIN(6), DRAW(3), LOSS(0)
}

private class Encounter(val mine: RPS, val theirs: RPS) {
    fun score(): Int {
        val matchResult = when (mine) {
            ROCK -> when (theirs) {
                ROCK -> DRAW
                PAPER -> LOSS
                SCISSORS -> WIN
            }

            PAPER -> when (theirs) {
                ROCK -> WIN
                PAPER -> DRAW
                SCISSORS -> LOSS
            }

            SCISSORS -> when (theirs) {
                ROCK -> LOSS
                PAPER -> WIN
                SCISSORS -> DRAW
            }
        }
        return mine.score + matchResult.score
    }
}

private class ActualStrategy(val theirs: RPS, val desiredResult: MatchResult) {
    fun score(): Int {
        val mine = when (desiredResult) {
            DRAW -> theirs
            WIN -> when (theirs) {
                ROCK -> PAPER
                PAPER -> SCISSORS
                SCISSORS -> ROCK
            }

            LOSS -> when (theirs) {
                ROCK -> SCISSORS
                PAPER -> ROCK
                SCISSORS -> PAPER
            }
        }
        return mine.score + desiredResult.score
    }
}
package de.dikodam.calendar

import de.dikodam.AbstractDay
import de.dikodam.executeTasks
import kotlin.time.ExperimentalTime

@ExperimentalTime
fun main() {
    Day05().executeTasks()
}

class Day05 : AbstractDay() {

    private val inputLines = readInputLines()
    private val stackDefinition = inputLines.takeWhile { it.isNotEmpty() }
    private val moveCommands = inputLines.drop(stackDefinition.size + 1)
        .map { parseMoveCommand(it) }

    override fun task1(): String {
        val stacks = buildStacks(stackDefinition)
        val restackedStacks = moveCommands.fold(stacks) { acc, mc ->
            mc.executeCraneMover9000(acc)
            acc
        }
        return restackedStacks
            .map { (_, deque) -> deque.removeFirst() }
            .joinToString(separator = "", prefix = "", postfix = "")
    }


    private fun buildStacks(wholeStackDef: List<String>): Map<Int, ArrayDeque<Char>> {
        val shortStackDef: List<String> = wholeStackDef.dropLast(1)
        val stacks = (1..9).associateWith { ArrayDeque<Char>() }
        val stackPositionsInLine = (1..34 step 4)
        val stacksWithPositions = stacks.keys.zip(stackPositionsInLine)
        for (i in shortStackDef.size - 1 downTo 0) {
            val line = shortStackDef[i]
            for ((stackNumber, position) in stacksWithPositions) {
                val c = line[position]
                if (c != ' ') {
                    stacks[stackNumber]!!.addFirst(c)
                }
            }
        }
        return stacks
    }

    private fun parseMoveCommand(line: String): MoveCommand {
        val moveCommandParts = line.split(" ")
        val amount = moveCommandParts[1].toInt()
        val from = moveCommandParts[3].toInt()
        val to = moveCommandParts[5].toInt()
        return MoveCommand(amount, from, to)
    }

    override fun task2(): String {
        val stacks = buildStacks(stackDefinition)
        val restackedStacks = moveCommands.fold(stacks) { acc, mc ->
            mc.executeCraneMover9001(acc)
            acc
        }
        return restackedStacks
            .map { (_, deque) -> deque.removeFirst() }
            .joinToString(separator = "", prefix = "", postfix = "")
    }

}

private data class MoveCommand(val amount: Int, val from: Int, val to: Int) {
    fun executeCraneMover9000(stacks: Map<Int, ArrayDeque<Char>>) {
        val fromStack = stacks[from]!!
        val toStack = stacks[to]!!
        repeat(amount) {
            val crate = fromStack.removeFirst()
            toStack.addFirst(crate)
        }
    }

    fun executeCraneMover9001(stacks: Map<Int, ArrayDeque<Char>>) {
        val fromStack = stacks[from]!!
        val toStack = stacks[to]!!
        val crates = generateSequence { fromStack.removeFirst() }
            .take(amount)
            .toList()
            .reversed()
        crates.forEach { crate -> toStack.addFirst(crate) }
    }
}



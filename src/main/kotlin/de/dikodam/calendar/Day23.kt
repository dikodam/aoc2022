package de.dikodam.calendar

import de.dikodam.*
import de.dikodam.Cardinal4Direction.*
import kotlin.time.ExperimentalTime


@ExperimentalTime
fun main() {
    Day23().executeTasks()
}

class Day23 : AbstractDay() {

    private val initalState = parseInputAs2dGrid { char, coords -> if (char == '#') Elf(coords, N) else null }

    override fun task1(): String {

        var gameState = GameSate(initalState)

        repeat(10) {
            gameState = gameState.next()
        }
        val positions = gameState.elves.map { it.position }
        val (minX, maxX, minY, maxY) = positions.minMaxBox()
        val area = (maxX - minX + 1) * (maxY - minY + 1)
        val emptyCount = area - positions.size

        return "$emptyCount"
    }

    override fun task2(): String {
        var currentGameSate = GameSate(initalState)
        var keepGoing = true
        var roundsDone = 0
        do {
            val nextGameSate = currentGameSate.next()
            keepGoing = !nextGameSate.isUnchanged(currentGameSate)
            currentGameSate = nextGameSate
            roundsDone++
        } while (keepGoing)
        return "$roundsDone"
    }
}

internal data class GameSate(val elves: Iterable<Elf>) {
    private val currentPositions: Set<Coordinates2D> = elves.map { it.position }.toSet()
    fun containsAny(positions: Iterable<Coordinates2D>): Boolean =
        positions.any { currentPositions.contains(it) }

    fun containsNone(positions: Iterable<Coordinates2D>): Boolean =
        positions.none { currentPositions.contains(it) }

    fun next(): GameSate {
        val proposalsByCoordinates = elves.map { it.proposeMove(this) }
            .groupBy { it.proposedPosition }
        val newState = proposalsByCoordinates
            .flatMap { (_, proposals) ->
                if (proposals.size == 1)
                    proposals.map { it.accept() }
                else
                    proposals.map { it.reject() }
            }
        return GameSate(newState)
    }

    fun isUnchanged(other: GameSate): Boolean {
        val myPositions = elves.map { it.position }.toSet()
        val otherPositions = other.elves.map { it.position }.toSet()
        return myPositions.all { otherPositions.contains(it) }
                && otherPositions.all { myPositions.contains(it) }
    }
}

internal fun Cardinal4Direction.toCardinal8Directions() = when (this) {
    N -> listOf(Cardinal8Direction.N, Cardinal8Direction.NE, Cardinal8Direction.NW)
    S -> listOf(Cardinal8Direction.S, Cardinal8Direction.SE, Cardinal8Direction.SW)
    W -> listOf(Cardinal8Direction.W, Cardinal8Direction.NW, Cardinal8Direction.SW)
    E -> listOf(Cardinal8Direction.E, Cardinal8Direction.NE, Cardinal8Direction.SE)
}

internal fun Cardinal4Direction.rotate() = when (this) {
    N -> S
    S -> W
    W -> E
    E -> N
}

internal data class Elf(val position: Coordinates2D, val direction: Cardinal4Direction) {
    fun proposeMove(currentState: GameSate): MoveProposal {
        val hasNeighbors = currentState.containsAny(position.all8neighbors())
        val nextDirection = direction.rotate()
        if (!hasNeighbors) {
            return MoveProposal(position, position, nextDirection)
        } else {
            var directionToTry = direction
            repeat(4) {
                val directionalNeighbors =
                    directionToTry.toCardinal8Directions().map { cd8 -> position + cd8.toCoords2D() }
                if (currentState.containsNone(directionalNeighbors)) {
                    return MoveProposal(position, position + directionToTry.toCoords2D(), nextDirection)
                }
                directionToTry = directionToTry.rotate()
            }
            return MoveProposal(position, position, nextDirection)
        }
    }
}

internal data class MoveProposal(
    private val currentPosition: Coordinates2D,
    val proposedPosition: Coordinates2D,
    private val nextDirection: Cardinal4Direction
) {
    fun reject(): Elf = Elf(currentPosition, nextDirection)
    fun accept(): Elf = Elf(proposedPosition, nextDirection)

}


package de.dikodam.calendar

import de.dikodam.AbstractDay
import de.dikodam.Coordinates2D
import de.dikodam.executeTasks
import kotlin.time.ExperimentalTime

@ExperimentalTime
fun main() {
    Day08().executeTasks()
}

class Day08 : AbstractDay() {
    val inputGrid = read2DGrid()

    override fun task1(): String {
        val minX = inputGrid.keys.minOf { it.x }
        val maxX = inputGrid.keys.maxOf { it.x }
        val minY = inputGrid.keys.minOf { it.y }
        val maxY = inputGrid.keys.maxOf { it.y }
        val rows = (minY..maxY).map { x ->
            inputGrid.filter { (coords, _) -> coords.x == x }
                .toList()
                .sortedBy { (coords, _) -> coords.y }
        }
        val cols = (minX..maxX).map { y ->
            inputGrid.filter { (coords, _) -> coords.y == y }
                .toList()
                .sortedBy { (coords, _) -> coords.x }
        }
        val visibleTrees = mutableSetOf<Coordinates2D>()
        rows.forEach { row ->
            var curTreeHeight = 0
            row.forEach { (coords, tree) ->
                if (tree > curTreeHeight) {
                    visibleTrees += coords
                    curTreeHeight = tree
                }
            }
            curTreeHeight = 0
            row.reversed().forEach { (coords, tree) ->
                if (tree > curTreeHeight) {
                    visibleTrees += coords
                    curTreeHeight = tree
                }
            }
        }
        cols.forEach { col ->
            var curTreeHeight = 0
            col.forEach { (coords, tree) ->
                if (tree > curTreeHeight) {
                    visibleTrees += coords
                    curTreeHeight = tree
                }
            }
            curTreeHeight = 0
            col.reversed().forEach { (coords, tree) ->
                if (tree > curTreeHeight) {
                    visibleTrees += coords
                    curTreeHeight = tree
                }
            }
        }
        return visibleTrees.size.toString()
    }

    override fun task2(): String {
        return ""
    }

    fun read2DGrid(): Map<Coordinates2D, Int> {
        val lines = readInputLines()
        return lines.flatMapIndexed { y, line ->
            line.mapIndexed { x, c ->
                Coordinates2D(x, y) to c.toString().toInt()
            }
        }.associate { it }
    }
}


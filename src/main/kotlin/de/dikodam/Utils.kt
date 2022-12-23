package de.dikodam

data class Coordinates2D(val x: Int, val y: Int) {
    operator fun plus(other: Coordinates2D) = Coordinates2D(x + other.x, y + other.y)

    fun all4Neighbors(): List<Coordinates2D> {
        return Cardinal4Direction.values().map { dir -> this + dir.toCoords2D() }
    }

    fun all8neighbors(): List<Coordinates2D> {
        return Cardinal8Direction.values().map { dir -> this + dir.toCoords2D() }
    }
}

enum class Cardinal4Direction {
    N, S, W, E;

    fun toCoords2D(): Coordinates2D = when (this) {
        N -> Coordinates2D(0, -1)
        S -> Coordinates2D(0, 1)
        W -> Coordinates2D(-1, 0)
        E -> Coordinates2D(1, 0)
    }
}

enum class Cardinal8Direction {
    N, S, W, E, NW, NE, SW, SE;

    fun toCoords2D(): Coordinates2D = when (this) {
        N -> Coordinates2D(0, -1)
        S -> Coordinates2D(0, 1)
        W -> Coordinates2D(-1, 0)
        E -> Coordinates2D(1, 0)
        NW -> N.toCoords2D() + W.toCoords2D()
        NE -> N.toCoords2D() + E.toCoords2D()
        SW -> S.toCoords2D() + W.toCoords2D()
        SE -> S.toCoords2D() + E.toCoords2D()
    }
}

data class Coordinates3D(val x: Int, val y: Int, val z: Int)

fun <T : Comparable<T>> min(pair: Pair<T, T>): T {
    val (a, b) = pair
    return if (a <= b) a else b
}

fun <T : Comparable<T>> max(pair: Pair<T, T>): T {
    val (a, b) = pair
    return if (a >= b) a else b
}

fun pointsBetween(low: Coordinates3D, high: Coordinates3D): Set<Coordinates3D> {
    return (low.x..high.x).flatMap { x ->
        (low.y..high.y).flatMap { y ->
            (low.z..high.z).map { z -> Coordinates3D(x, y, z) }
        }
    }.toSet()
}

fun String.hexToBinString(): String =
    this.map { char -> char.digitToInt(16) }.map { Integer.toBinaryString(it) }.map { it.padStart(4, '0') }
        .joinToString("")

fun String.splitAfter(count: Int): Pair<String, String> = take(count) to drop(count)

fun <T> Iterable<T>.splitAfter(count: Int): Pair<List<T>, List<T>> = take(count) to drop(count)

fun <T> List<T>.divideAt(splitter: T): List<List<T>> {
    var remaining = this
    var accumulator: MutableList<List<T>> = mutableListOf()
    while (remaining.isNotEmpty()) {
        val list = remaining.takeWhile { it != splitter }
        accumulator += list
        remaining = remaining.drop(list.size + 1)
    }
    return accumulator
}

fun IntRange.containsRange(other: IntRange): Boolean = this.first <= other.first && this.last >= other.last

fun Collection<Coordinates2D>.minMaxBox() =
    MinMaxBox(minX = minOf { it.x }, maxX = maxOf { it.x }, minY = minOf { it.y }, maxY = maxOf { it.y })


data class MinMaxBox(val minX: Int, val maxX: Int, val minY: Int, val maxY: Int)


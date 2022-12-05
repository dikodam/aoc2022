package de.dikodam

data class Coordinates2D(val x: Int, val y: Int) {
    operator fun plus(other: Coordinates2D) =
        Coordinates2D(x + other.x, y + other.y)
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
    this.map { char -> char.digitToInt(16) }
        .map { Integer.toBinaryString(it) }
        .map { it.padStart(4, '0') }
        .joinToString("")

fun String.splitAfter(count: Int): Pair<String, String> =
    take(count) to drop(count)

fun <T> Iterable<T>.splitAfter(count: Int): Pair<List<T>, List<T>> =
    take(count) to drop(count)

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

fun IntRange.containsRange(other: IntRange): Boolean =
    this.first <= other.first && this.last >= other.last


fun main() {
    val input = """1000
2000
3000

4000

5000
6000

7000
8000
9000

10000""".lines()
    input.divideAt("").also { println(it) }
}


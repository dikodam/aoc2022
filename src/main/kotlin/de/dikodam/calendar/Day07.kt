package de.dikodam.calendar

import de.dikodam.AbstractDay
import de.dikodam.executeTasks
import kotlin.time.ExperimentalTime

@ExperimentalTime
fun main() {
    Day07().executeTasks()
}

class Day07 : AbstractDay() {

    private val terminalHistory = readInputLines().map { Day07TerminalLine.parseLine(it) }
//    private val terminalHistory = """${'$'} cd /
//${'$'} ls
//dir a
//14848514 b.txt
//8504156 c.dat
//dir d
//${'$'} cd a
//${'$'} ls
//dir e
//29116 f
//2557 g
//62596 h.lst
//${'$'} cd e
//${'$'} ls
//584 i
//${'$'} cd ..
//${'$'} cd ..
//${'$'} cd d
//${'$'} ls
//4060174 j
//8033020 d.log
//5626152 d.ext
//7214296 k""".lines().map { Day07TerminalLine.parseLine(it) }


    override fun task1(): String {
        val dirs: Map<String, List<Day07TerminalLine>> = buildTree(terminalHistory)

        val dingens = dirs.asIterable()
            .sortedBy { (_, list) -> list.size }

        val buffer = mutableMapOf<String, Int>()

        for ((key, list) in dingens) {
            buffer[key] = dirs.computeSizeOf(key)
        }

        return buffer.values
            .filter { it <= 100_000 }
            .sum()
            .toString()
    }

    override fun task2(): String {
        return ""
    }

    val dirSizes = mutableMapOf<String, Int>()
    private fun Map<String, List<Day07TerminalLine>>.computeSizeOf(key: String): Int {
        return if (dirSizes[key] != null) {
            dirSizes[key]!!
        } else {
            val children = this[key]!!
            val size = children.sumOf {
                when (it) {
                    is DirectoryDescriptor -> this.computeSizeOf(it.name)
                    is FileDescriptor -> it.size
                    else -> error("impossible")
                }
            }
            dirSizes[key] = size
            size
        }
    }

    private fun buildTree(lines: List<Day07TerminalLine>): Map<String, List<Day07TerminalLine>> {
        var currentDir = ""
        val memory: MutableMap<String, MutableList<Day07TerminalLine>> = mutableMapOf()
        val queue = ArrayDeque(lines)

        while (queue.isNotEmpty()) {
            when (val currentLine = queue.removeFirst()) {
                is CdCommand -> currentDir = currentLine.goal
                LsCommand -> {
                    val treeElements = queue.removeWhile { it is DirectoryDescriptor || it is FileDescriptor }
                        .toMutableList()
                    memory.merge(currentDir, treeElements) { l1, l2 -> l1.addAll(l2); l1 }
                }

                else -> error("unexpected command")
            }
        }
        return memory
    }

}

private fun MutableMap<String, MutableList<Day07TerminalLine>>.extractChildrenFor(key: String): List<NavTreeElement> {
    val mem = this
    val children = mem[key]!!.map {
        when (it) {
            is FileDescriptor -> it.toFile()
            is DirectoryDescriptor -> Dir(key, extractChildrenFor(it.name))
            else -> error("this really shouldn't happen")
        }
    }
    return children
}


sealed class NavTreeElement {
    abstract val name: String
    abstract fun size(): Int
}

data class Dir(override val name: String, val children: List<NavTreeElement>) : NavTreeElement() {
    override fun size(): Int = children.sumOf { it.size() }
}

data class File(override val name: String, val size: Int) : NavTreeElement() {
    override fun size(): Int = this.size
}


sealed class Day07TerminalLine {

    companion object {
        fun parseLine(line: String): Day07TerminalLine =
            when {
                line.startsWith("$ cd") -> CdCommand.parseLine(line)
                line.startsWith("$ ls") -> LsCommand
                line.startsWith("dir") -> DirectoryDescriptor.parseLine(line)
                else -> FileDescriptor.parseLine(line)
            }
    }
}

data class CdCommand(val goal: String) : Day07TerminalLine() {
    companion object {
        fun parseLine(line: String): Day07TerminalLine {
            // $ cd /
            val (_, _, goal) = line.split(" ")
            return CdCommand(goal)
        }
    }
}

object LsCommand : Day07TerminalLine()
data class DirectoryDescriptor(val name: String) : Day07TerminalLine() {
    companion object {
        fun parseLine(line: String): Day07TerminalLine {
            // dir d
            val (_, dirName) = line.split(" ")
            return DirectoryDescriptor(dirName)
        }
    }
}

data class FileDescriptor(val name: String, val size: Int) : Day07TerminalLine() {
    companion object {
        fun parseLine(line: String): Day07TerminalLine {
            // 123 a.txt
            val (fileSize, fileName) = line.split(" ")
            return FileDescriptor(fileName, fileSize.toInt())
        }
    }
}

fun FileDescriptor.toFile() = File(name, size)

fun <T> ArrayDeque<T>.remove(i: Int) = (0 until i).map { this.removeFirst() }

fun <T> ArrayDeque<T>.removeWhile(predicate: (T) -> Boolean): List<T> {
    val acc = mutableListOf<T>()
    while (if (firstOrNull() == null) false else predicate(first())) {
        acc += removeFirst()
    }
    return acc
}

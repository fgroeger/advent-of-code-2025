package de.fabiangroeger.adventofcode

import kotlin.math.max
import kotlin.math.min
import kotlin.test.Test

class Day4PrintingDepartment {
    @Test
    fun executePartOne() {
        val output = with(grid) {
            getReachablePaperRolls().count()
        }
        println(output)
    }

    @Test
    fun executePartTwo() {
        val output = with(grid) {
            buildList {
                var reachableObjects = getReachablePaperRolls()
                while (reachableObjects.isNotEmpty()) {
                    reachableObjects.forEach {
                        removeObject(it)
                    }
                    add(reachableObjects.count())

                    reachableObjects = getReachablePaperRolls()
                }
            }.sum()
        }
        println(output)
    }

    companion object {
        private val exampleGrid = """
            ..@@.@@@@.
            @@@.@.@.@@
            @@@@@.@.@@
            @.@@@@..@.
            @@.@@@@.@@
            .@@@@@@@.@
            .@.@.@.@@@
            @.@@@.@@@@
            .@@@@@@@@.
            @.@.@@@.@.
        """.trimIndent().let { Grid.fromString(it) }

        private val grid = """
            YOUR
            INPUT
            HERE
        """.trimIndent().let { Grid.fromString(it) }
    }
}

data class GridIndex(
    val row: Int,
    val column: Int
)

data class PaperRoll(
    val index: GridIndex,
    var isPaperRollAvailable: Boolean
)

data class Grid(
    val grid: MutableList<PaperRoll>
) {
    fun getAvailablePaperRollIndexes(): List<GridIndex> {
        return grid.mapNotNull { paperRoll ->
            if (paperRoll.isPaperRollAvailable) {
                paperRoll.index
            } else {
                null
            }
        }
    }

    fun getAdjacentAvailablePaperRollIndexes(index: GridIndex): List<GridIndex> {
        val rowRange = max(0, index.row - 1)..min(index.row + 1, grid.size - 1)
        val columnRange = max(0, index.column - 1)..min(index.column + 1, grid.size - 1)

        return grid.filter {
            it.isPaperRollAvailable && it.index.row in rowRange && it.index.column in columnRange
        }.map {
            it.index
        }
    }

    fun getReachablePaperRolls(): List<GridIndex> {
        return getAvailablePaperRollIndexes()
            .filter { index ->
                getAdjacentAvailablePaperRollIndexes(index).count() < 5 // +1 for the element itself
            }
    }

    fun removeObject(index: GridIndex) {
        grid.first { it.index == index }.isPaperRollAvailable = false
    }

    companion object {
        fun fromString(grid: String): Grid {
            return Grid(
                grid.lines().flatMapIndexed { rowIndex, line ->
                    line.mapIndexed { columnIndex, obj ->
                        val isPaperRollAvailable = when (obj) {
                            '.' -> false
                            '@' -> true
                            else -> throw IllegalArgumentException("Unsupported char $obj")
                        }

                        PaperRoll(
                            index = GridIndex(
                                row = rowIndex,
                                column = columnIndex
                            ),
                            isPaperRollAvailable
                        )
                    }
                }.toMutableList()
            )
        }
    }
}
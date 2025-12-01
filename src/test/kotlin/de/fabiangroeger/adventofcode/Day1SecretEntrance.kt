package de.fabiangroeger.adventofcode

import de.fabiangroeger.adventofcode.Rotation.Direction.LEFT
import de.fabiangroeger.adventofcode.Rotation.Direction.RIGHT
import kotlin.math.abs
import kotlin.test.Test

class Day1SecretEntrance {
    @Test
    fun executePartOne() {
        val dial = Dial()

        val result = rotations
            .map {
                dial.rotate(it)
                dial.currentNumber
            }.count { it == 0 }

        println(result)
    }

    @Test
    fun executePartTwo() {
        val dial = Dial()

        rotations.forEach {
            dial.rotate(it)
        }
        val result = dial.zeroHitted

        println(result)
    }

    companion object {
        private val rotations = """
            YOUR
            INPUT
            HERE
        """.trimIndent().lines().map {
            Rotation.parseFromString(it)
        }

        private val exampleRotations = """
            L68
            L30
            R48
            L5
            R60
            L55
            L1
            L99
            R14
            L82
        """.trimIndent().lines().map {
            Rotation.parseFromString(it)
        }
    }
}

class Dial {
    var currentNumber: Int = INITIAL_NUMBER
    var zeroHitted: Int = 0

    fun rotate(rotation: Rotation) {
        var clicksRemaining = rotation.amount

        while (clicksRemaining > 0) {
            when (rotation.direction) {
                LEFT -> {
                    when (currentNumber) {
                        0 -> {
                            currentNumber = 99
                        }
                        else -> {
                            currentNumber -= 1
                        }
                    }
                }

                RIGHT -> {
                    when (currentNumber) {
                        99 -> {
                            currentNumber = 0
                        }
                        else -> {
                            currentNumber += 1
                        }
                    }
                }
            }

            if (currentNumber == 0) {
                zeroHitted += 1
            }

            clicksRemaining -= 1
        }
    }

    companion object {
        private const val INITIAL_NUMBER = 50
    }
}

data class Rotation(
    val direction: Direction,
    val amount: Int
) {
    enum class Direction {
        LEFT,
        RIGHT,
        ;
    }

    companion object {
        fun parseFromString(string: String): Rotation {
            val regex = Regex("([L|R])([0-9]+)")
            val matches = regex.find(string)!!

            require(matches.groupValues.size == 3)

            val direction = when (matches.groupValues[1]) {
                "L" -> LEFT
                "R" -> RIGHT
                else -> throw IllegalArgumentException("Unknown direction.")
            }

            val amount = matches.groupValues[2].toInt()

            return Rotation(
                direction,
                amount
            )
        }
    }
}
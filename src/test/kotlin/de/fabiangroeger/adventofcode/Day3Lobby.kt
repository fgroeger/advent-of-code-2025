package de.fabiangroeger.adventofcode

import kotlin.test.Test

class Day3Lobby {
    @Test
    fun executePartOne() {
        val output = banks.sumOf {
            it.findMaximumJoltage(2).toLong()
        }
        println(output)
    }

    @Test
    fun executePartTwo() {
        val output = banks.sumOf {
            it.findMaximumJoltage(12).toLong()
        }
        println(output)
    }

    companion object {
        private val exampleBanks = """
            987654321111111
            811111111111119
            234234234234278
            818181911112111
        """.trimIndent().lines().map { BatteryBank.fromString(it) }

        private val banks = """
            YOUR
            INPUT
            HERE
        """.trimIndent().lines().map { BatteryBank.fromString(it) }
    }
}

data class Battery(
    val joltage: String
) {
    init {
        require(joltage.toInt() in 1..9)
    }
}

data class BatteryBank(
    val batteries: List<Battery>
) {
    fun findLargestDigit(remainingBatteries: Int): String {
        return batteries
            .dropLast(remainingBatteries)
            .maxOf {
                it.joltage.toInt()
            }
            .toString()
    }

    fun findMaximumJoltage(numberOfBatteries: Int): String {
        if (numberOfBatteries == 0 || batteries.isEmpty()) {
            return ""
        }

        val index = batteries.indexOfFirst {
            val largestDigit = findLargestDigit(numberOfBatteries - 1)
            it.joltage == largestDigit
        }

        val remainingBatteries = BatteryBank(
            batteries.drop(index + 1)
        )
        val maximumJoltage = batteries[index].joltage + remainingBatteries.findMaximumJoltage(numberOfBatteries - 1)
        return maximumJoltage
    }

    companion object {
        fun fromString(bank: String): BatteryBank {
            return BatteryBank(
                bank.map { Battery(it.toString()) }
            )
        }
    }
}
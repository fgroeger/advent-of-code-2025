package de.fabiangroeger.adventofcode

import kotlin.test.Test

class Day2GiftShop {
    @Test
    fun executePartOne() {
        val invalidProductIds = productIdRanges.flatMap { range ->
            (range.productIds).mapNotNull { productId ->
                if (productId.isValidPartOne()) {
                    null
                } else {
                    productId
                }
            }
        }

        val result = invalidProductIds.sumOf { it.value }

        println(result)
    }

    @Test
    fun executePartTwo() {
        val invalidProductIds = productIdRanges.flatMap { range ->
            (range.productIds).mapNotNull { productId ->
                if (productId.isValidPartTwo()) {
                    null
                } else {
                    productId
                }
            }
        }

        val result = invalidProductIds.sumOf { it.value }

        println(result)
    }

    companion object {
        private val exampleProductIdRanges = """
            11-22,95-115,998-1012,1188511880-1188511890,222220-222224,1698522-1698528,446443-446449,38593856-38593862,565653-565659,824824821-824824827,2121212118-2121212124
        """.trimIndent().split(",").map { ProductIdRange.fromString(it) }

        private val productIdRanges = """
            YOUR,RANGES,HERE
        """.trimIndent().split(",").map { ProductIdRange.fromString(it) }
    }
}

@JvmInline
value class ProductId(val value: Long) {
    fun isValidPartOne(): Boolean {
        return !hasOnlyDuplicates()
    }

    fun isValidPartTwo(): Boolean {
        return !hasOneOrMoreDuplicatesOnly()
    }

    private fun hasOnlyDuplicates(): Boolean {
        var haystack = value.toString()
        var needle = ""

        while (haystack.isNotEmpty()) {
            needle += haystack.first()
            haystack = haystack.drop(1)

            if (haystack == needle) {
                return true
            }
        }

        return false
    }

    private fun hasOneOrMoreDuplicatesOnly(): Boolean {
        var haystack = value.toString()
        var needle = ""

        if (haystack.length == 1) {
            return false
        }

        while (haystack.length > needle.length) {
            needle += haystack.first()
            haystack = haystack.drop(1)

            // We can only find an invalid id if the haystack length is a multiple of the needle length
            if (haystack.length % needle.length > 0) {
                continue
            }

            // If our search pattern is two digits and our remainder from the original sequence is four digits, we expect two matches.
            // example: 55  ->  5555
            val expectedMatches = haystack.length / needle.length
            val regex = Regex(needle)

            val matches = regex.findAll(haystack)
            if (matches.count() == expectedMatches) {
                return true
            }
        }

        return false
    }
}

data class ProductIdRange(
    val productIds: List<ProductId>
) {
    companion object {
        fun fromString(string: String): ProductIdRange {
            val (from, to) = string.split("-", limit = 2)
            val range = (from.toLong() .. to.toLong()).map { ProductId(it) }
            return ProductIdRange(range)
        }
    }
}
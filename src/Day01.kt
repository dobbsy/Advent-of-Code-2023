fun main() {
    fun part1_iterative(input: List<String>): Int {
        var result = 0
        for (line in input) {
            val firstDigit = line.first { it.isDigit() }
            val lastDigit = line.last { it.isDigit() }
            result += ("" + firstDigit + lastDigit).toInt()
        }

        return result
    }

    fun part1(input: List<String>) = input.map { "" + it.first(Char::isDigit) + it.last(Char::isDigit) }
            .sumOf(String::toInt)

    fun part2(input: List<String>): Int {
        val digitMap = hashMapOf(
                "one" to '1',
                "two" to '2',
                "three" to '3',
                "four" to '4',
                "five" to '5',
                "six" to '6',
                "seven" to '7',
                "eight" to '8',
                "nine" to '9'
        )
        val digitStringSet: Set<String> = digitMap.keys

        var result = 0

        for (line in input) {
            var (indexFirstDigit, firstDigit) = line.withIndex()
                    .firstOrNull { it.value.isDigit() } ?: IndexedValue(Int.MAX_VALUE, null)
            val (indexFirstDigitString, firstDigitString) = line.findAnyOf(digitStringSet) ?: (Int.MAX_VALUE to null)

            if (indexFirstDigitString < indexFirstDigit) {
                firstDigit = digitMap[firstDigitString]
            }

            var (indexLastDigit, lastDigit) = line.withIndex()
                    .lastOrNull { it.value.isDigit() } ?: IndexedValue(Int.MIN_VALUE, null)
            val (indexLastDigitString, lastDigitString) = line.findLastAnyOf(digitStringSet) ?: (Int.MIN_VALUE to null)

            if (indexLastDigitString > indexLastDigit) {
                lastDigit = digitMap[lastDigitString]
            }

            result += ("" + firstDigit + lastDigit).toInt()
        }

        return result
    }

    // test if implementation meets criteria from the description, like:
    val testInputPart1 = readInput("Day01_test_part1")
    check(part1(testInputPart1) == 142)
    val testInputPart2 = readInput("Day01_test_part2")
    check(part2(testInputPart2) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}

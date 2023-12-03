fun main() {
    fun part1(input: List<String>): Long {
        var res = 0L

        for ((rowIndex, line) in input.withIndex()) {
            var inNumber = false
            var startIndex = 0 // the first index of the current number
            var endIndex: Int  // the last index of the number

            for ((columnIndex, c) in line.withIndex()) {
                if (inNumber) {
                    // if the last char was a digit and the current char isn't, the last char was the last digit of a number
                    if (!c.isDigit()) {
                        inNumber = false

                        // if we were in a number until now, but now we aren't, we have to look if we should include the number
                        endIndex = columnIndex - 1
                        if (hasSymbolNeighbor(input, rowIndex, startIndex, endIndex)) {
                            res += line.substring(startIndex, endIndex + 1).toInt()
                        }
                    }
                    // if we are currently in a number and the current char is the last one of the line,
                    // it also is the last char of a number
                    else if (columnIndex == line.lastIndex) {
                        endIndex = columnIndex
                        if (hasSymbolNeighbor(input, rowIndex, startIndex, endIndex)) {
                            res += line.substring(startIndex, endIndex + 1).toInt()
                        }
                    }
                } else {
                    // if we are not currently in a number, but c is a digit, a new number starts here
                    if (c.isDigit()) {
                        inNumber = true
                        startIndex = columnIndex
                    }
                }
            }
        }

        return res
    }


    fun part2(input: List<String>): Int {
        var res = 0

        for ((rowIndex, line) in input.withIndex()) {
            for ((columnIndex, c) in line.withIndex()) {
                if (c == '*') {
                    val numberNeighbors = getNeighboringNumbers(input, rowIndex, columnIndex)
                    if (numberNeighbors.size == 2) {
                        res += numberNeighbors[0] * numberNeighbors[1]
                    }
                }
            }
        }

        return res
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361L)
    check(part2(testInput) == 467835)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}

fun hasSymbolNeighbor(input: List<String>, rowIndex: Int, columnStartIndex: Int, columnEndIndex: Int): Boolean {
    // let's first check the left and right neighbors
    if (columnStartIndex > 0 && input[rowIndex][columnStartIndex - 1].isSymbol()) return true
    if (columnEndIndex < input[rowIndex].lastIndex && input[rowIndex][columnEndIndex + 1].isSymbol()) return true

    // for the row above and below the current line, any character with an index included in [columnStartIndex - 1, columnEndIndex + 1]
    // counts as a neighbor

    if (rowIndex > 0) {
        val lineAbove = input[rowIndex - 1]
        val neighborString = lineAbove.substring(
            (columnStartIndex - 1).coerceAtLeast(0),
            (columnEndIndex + 2).coerceAtMost(lineAbove.length)
        )
        if (neighborString.hasSymbol()) return true
    }

    if (rowIndex < input.lastIndex) {
        val lineBelow = input[rowIndex + 1]
        val neighborString = lineBelow.substring(
            (columnStartIndex - 1).coerceAtLeast(0),
            (columnEndIndex + 2).coerceAtMost(lineBelow.length)
        )
        if (neighborString.hasSymbol()) return true
    }

    return false
}

fun Char.isSymbol(): Boolean {
    return !isLetterOrDigit() && this != '.' && !isWhitespace()
}

fun CharSequence.hasSymbol(): Boolean {
    return this.any { it.isSymbol() }
}

fun getNeighboringNumbers(input: List<String>, rowIndex: Int, columnIndex: Int): List<Int> {
    val res = mutableListOf<Int>()

    val coordinates = mutableListOf<Pair<Int, Int>>()

    for (x in (columnIndex - 1)..(columnIndex + 1)) {
        for (y in (rowIndex - 1)..(rowIndex + 1)) {
            if (x > 0 && y > 0 && y <= input.lastIndex && x <= input[y].lastIndex) {
                coordinates += x to y
            }
        }
    }

    for ((x, y) in coordinates) {
        val c = input[y][x]
        if (c.isDigit()) res += c.digitToInt()
    }

    return res
}

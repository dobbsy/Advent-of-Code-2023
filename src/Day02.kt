import kotlin.math.max

fun main() {
    fun processLine(line: String): Game {
        val lineMatch = """Game (\d*): (.*)""".toRegex().matchEntire(line)
            ?: throw Exception("'$line' did not match the regex")
        val groups = lineMatch.groups
        val id = groups[1]!!.value.toInt()
        val draws = groups[2]!!.value

        return Game(id, draws)
    }

    fun processDraw(drawString: String): Draw {
        val drawMatch = """(\d*) (.*)""".toRegex().matchEntire(drawString.trim())
            ?: throw Exception("draw '$drawString' didn't match the regex")
        val drawGroups = drawMatch.groups
        val amount = drawGroups[1]!!.value.toInt()
        val color = drawGroups[2]!!.value

        return Draw(color, amount)
    }

    fun part1(input: List<String>): Int {
        val colorMap = hashMapOf(
            "red" to 12,
            "green" to 13,
            "blue" to 14
        )

        var res = 0
        line@for (line in input) {
            val (id, draws) = processLine(line)

            for (drawString in draws.split(';', ',')) {
                val (color, amount) = processDraw(drawString)
                if (amount > colorMap[color]!!) continue@line
            }

            res += id
        }

        return res
    }

    fun part2(input: List<String>): Int {
        var res = 0

        for (line in input) {
            val colorMap = mutableMapOf(
                "red" to 0,
                "green" to 0,
                "blue" to 0
            )

            val (_, draws) = processLine(line)
            for (drawString in draws.split(',', ';')) {
                val (color, amount) = processDraw(drawString)
                colorMap[color] = max(colorMap[color] ?: 0, amount)
            }

            res += colorMap.values.reduce { acc, amount -> acc * amount }
        }

        return res
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)
    check(part2(testInput) == 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}

data class Draw(val color: String, val amount: Int)
data class Game(val id: Int, val draws: String)
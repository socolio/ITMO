package ru.oivanov.crypto.alphabet

import ru.oivanov.crypto.alphabet.statistics.AlphabetStatistics


class ShiftedAlphabetGenerator : AlphabetGenerator {

    override fun generate(): Alphabet {
        val shift = Alphabet.random.nextInt(Alphabet.alphabet.length - 1) + 1
        return generate(shift)
    }

    fun generate(shift: Int): Alphabet {
        val s = if(shift < 0) Alphabet.size + shift else shift
        return Alphabet((Alphabet.alphabet.substring(s) + Alphabet.alphabet.substring(0, s)).toCharArray())
    }

    override fun byStatistic(statistics: AlphabetStatistics): Alphabet {
        val source = AlphabetStatistics.english
        val target = statistics

        var shifts = Array(Alphabet.size) { 0f }

        for(i in 0 until Alphabet.size){
            var shift = target.sorted[i].first.toInt() - source.sorted[i].first.toInt()
            if(shift < 0){
                shift += Alphabet.size
            }

            shifts[shift] += target.sorted[i].second
        }

        val shift = shifts.mapIndexed { i, f -> Pair(i, f) }.sortedByDescending { it.second }[0].first

        return generate(-shift)
    }
}
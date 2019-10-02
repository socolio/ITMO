package ru.oivanov.crypto.alphabet.statistics

import ru.oivanov.crypto.alphabet.Alphabet


class StatisticsCollector {

    private val counts = IntArray(Alphabet.alphabet.length)
    private var size = 0

    fun collect(c: Char){
        counts[c.toInt() - 'a'.toInt()]++
        size++
    }

    fun getStatistics() : AlphabetStatistics {
        return AlphabetStatistics(counts.map { f -> f.toFloat() / size}.toFloatArray())
    }
}
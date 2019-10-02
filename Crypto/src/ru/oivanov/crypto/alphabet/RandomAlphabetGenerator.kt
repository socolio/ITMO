package ru.oivanov.crypto.alphabet

import ru.oivanov.crypto.alphabet.statistics.AlphabetStatistics


class RandomAlphabetGenerator : AlphabetGenerator {

    override fun generate(): Alphabet {
        val list = Alphabet.alphabet.toCharArray().toMutableList()
        list.shuffle()
        return Alphabet(list.toCharArray())
    }

    override fun byStatistic(statistics: AlphabetStatistics): Alphabet {
        val source = AlphabetStatistics.english

        val chars = CharArray(Alphabet.size) { 'a' }

        for (i in 0 until Alphabet.size){
            chars[statistics.sorted[i].first.toInt() - 'a'.toInt()] = source.sorted[i].first
        }

        return Alphabet(chars)
    }
}
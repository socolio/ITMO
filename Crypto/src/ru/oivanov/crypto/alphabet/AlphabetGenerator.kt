package ru.oivanov.crypto.alphabet

import ru.oivanov.crypto.alphabet.statistics.AlphabetStatistics


interface AlphabetGenerator {

    fun generate() : Alphabet

    fun byStatistic(statistics: AlphabetStatistics) : Alphabet
}
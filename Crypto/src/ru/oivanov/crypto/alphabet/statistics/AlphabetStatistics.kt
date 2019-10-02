package ru.oivanov.crypto.alphabet.statistics

import ru.oivanov.crypto.alphabet.Alphabet


class AlphabetStatistics(val frequency: FloatArray) {

    val sorted = frequency.mapIndexed{ i, f -> Pair(i, f) }.sortedByDescending { it.second }.map { Pair((it.first + 'a'.toInt()).toChar(), it.second) }.toTypedArray()

    companion object {

        val english = AlphabetStatistics(Alphabet.aliceFrequency.value)

    }
}
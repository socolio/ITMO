package ru.oivanov.crypto.algorithm

import ru.oivanov.crypto.alphabet.Alphabet
import ru.oivanov.crypto.alphabet.AlphabetGenerator
import ru.oivanov.crypto.alphabet.statistics.StatisticsCollector


object Vigenere {

    fun encrypt(text: String, alphabets: Array<Alphabet>) : String {
        val encrypted = StringBuilder()
        var j = 0
        text.toLowerCase().forEachIndexed { i, c ->
            when (c) {
                in 'a'..'z' -> {
                    val e = alphabets[j % alphabets.size][c]
                    encrypted.append(e)
                    j++
                }
                else -> encrypted.append(c)
            }
        }

        return encrypted.toString()
    }

    fun encrypt(text: String, alphabetsCount: Int, alphabetGenerator: AlphabetGenerator): String {
        val alphabets = Array(alphabetsCount) { alphabetGenerator.generate() }
        return encrypt(text, alphabets)
    }

    fun decrypt(encrypted: String, alphabetsCount: Int, alphabetGenerator: AlphabetGenerator): Pair<String, String> {
        val statisticsCollectors = Array(alphabetsCount) {
            StatisticsCollector()
        }

        var j = 0
        encrypted.toLowerCase().forEachIndexed { i, c ->
            if (c in 'a'..'z') {
                statisticsCollectors[j % alphabetsCount].collect(c)
                j++
            }
        }

        val alphabets = statisticsCollectors.map {
            val statistics = it.getStatistics()
            alphabetGenerator.byStatistic(statistics)
        }.toTypedArray()

        return Pair(encrypt(encrypted, alphabets), alphabets.joinToString(""))
    }

}
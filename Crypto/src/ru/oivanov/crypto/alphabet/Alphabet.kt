package ru.oivanov.crypto.alphabet

import java.util.*


class Alphabet(var array: CharArray) {

    operator fun get(i: Int) = array[i]
    operator fun get(c: Char) = get(c.toInt() - 'a'.toInt())

    override fun toString(): String {
        return get('a').toString()
    }

    companion object {

        val alphabet = "abcdefghijklmnopqrstuvwxyz"

        //val frequency = floatArrayOf(8.2f, 1.5f, 2.8f, 4.3f, 12.7f, 2.2f, 2.0f, 6.1f, 7.0f, 0.2f, 0.8f, 4.0f, 2.4f, 6.7f, 7.5f, 1.9f, 0.1f, 6.0f, 6.3f, 9.1f, 2.8f, 1.0f, 2.4f, 0.2f, 2.0f, 0.1f)
        val frequency = floatArrayOf(8.17f, 1.49f, 2.78f, 4.25f, 12.70f, 2.23f, 2.02f, 6.09f, 6.97f, 0.15f, 0.77f, 4.03f, 2.41f, 6.75f, 7.51f, 1.93f, 0.10f, 5.99f, 6.33f, 9.06f, 2.76f, 0.98f, 2.36f, 0.15f, 1.97f, 0.07f)

        val aliceFrequency = lazy {
            doubleArrayOf(8.162261, 1.3701302, 2.2256489, 4.5766993, 12.603341, 1.8578038, 2.3510506, 6.8497224, 6.974195, 0.13561967, 1.0756683, 4.37327, 1.9544095, 6.509744, 7.5631185, 1.4156464, 0.1941405, 5.0476527, 6.036004, 9.926246, 3.2177162, 0.784922, 2.4838834, 0.13747747, 2.101176, 0.07245435).map { it.toFloat() }.toFloatArray()
        }

        val size = alphabet.length
        val random = Random()
    }
}
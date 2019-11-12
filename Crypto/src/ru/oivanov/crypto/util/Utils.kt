package ru.oivanov.crypto.util

import java.nio.ByteBuffer


object Utils {

    fun getPartOfCoincidences(s1: String, s2: String): Float {
        var equal = 0
        var sum = 0

        s1.forEachIndexed { i, c ->
            if (c in 'a'..'z') {
                sum++

                if (s1[i].equals(s2[i], true)) {
                    equal++
                }
            }
        }

        return equal.toFloat() / sum
    }

    fun longToBytes(x: Long): ByteArray {
        val buffer = ByteBuffer.allocate(java.lang.Long.BYTES)
        buffer.putLong(x)
        return buffer.array()
    }

    fun bytesToLong(bytes: ByteArray): Long {
        val buffer = ByteBuffer.allocate(java.lang.Long.BYTES)
        buffer.put(bytes)
        buffer.flip()
        return buffer.long
    }
}

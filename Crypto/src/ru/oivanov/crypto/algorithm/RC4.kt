package ru.oivanov.crypto.algorithm

import java.io.InputStream

object RC4 {

    private fun generatePermutation(key: String, bytesCount: Int): IntArray {
        val size = 1 shl (bytesCount * 8)
        val perm = IntArray(size) { it }

        var j = 0
        for (i in 0 until size) {
            j = (j + perm[i] + key[i % key.length].toInt()) % size

            val temp = perm[j]
            perm[j] = perm[i]
            perm[i] = temp
        }

        return perm
    }

    fun modifyStream(key: String, bytesCount: Int, input: InputStream, output: (Int) -> Unit) {
        val perm = generatePermutation(key, bytesCount)

        var i = 0
        var j = 0
        while (input.available() != 0) {
            i = (i + 1) % 256
            j = (j + perm[i]) % 256

            val temp = perm[j]
            perm[j] = perm[i]
            perm[i] = temp

            val p = perm[(perm[i] + perm[j]) % 256]

            for(k in 0 until bytesCount){
                val byte = input.read()

                if(byte == -1){
                    return
                }

                val a = (p shr (k * 8)) and 0xFF
                output(byte xor a)
            }
        }

        input.close()
    }
}
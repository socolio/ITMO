package ru.oivanov.crypto.test

import ru.oivanov.crypto.algorithm.COMP128
import ru.oivanov.crypto.util.joinToHEXString


fun main() {
    val rand = "super_random_key".toByteArray()
    val clientKey = "super_secret_key".toByteArray()

    val formData = COMP128.getFormData(rand, clientKey)

    val sessionResponse = COMP128.a3(formData)
    val sessionKey = COMP128.a8(formData)

    println("SRES: ${sessionResponse.joinToHEXString()}")
    println("SKEY: ${sessionKey.joinToHEXString()}\n")

    val a5 = COMP128.A5(sessionKey)

    for (i in 0 until 100){
        a5.initFrame(i)
        val (decodeKey, encodeKey) = a5.nextKeys()
        println("FRAME #$i: ${decodeKey.joinToHEXString()} | ${encodeKey.joinToHEXString()}")
    }

}
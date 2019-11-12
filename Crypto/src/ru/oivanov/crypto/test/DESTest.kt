package ru.oivanov.crypto.test

import ru.oivanov.crypto.algorithm.DES
import ru.oivanov.crypto.util.toByteArray
import ru.oivanov.crypto.util.toULongArray


@ExperimentalUnsignedTypes
fun main() {
    val key = DES.key("secret1")

    val data = "super secret message!!!!"
    println(data)

    val encrypted = DES.encrypt(data.toByteArray().toULongArray(), key)
    println(encrypted.joinToString(" "))

    val decrypted = String(DES.decrypt(encrypted, key).toByteArray())
    println(decrypted)
}
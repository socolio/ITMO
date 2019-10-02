package ru.oivanov.crypto

import ru.oivanov.crypto.algorithm.DES


@ExperimentalUnsignedTypes
fun main() {
    val key = DES.key("kkostin")

    val data = "hello world"
    println(data)

    val encrypted = DES.encrypt(data.toByteArray().toULongArray(), key)
    println(encrypted.joinToString(" "))

    val decrypted = String(DES.decrypt(encrypted, key).toByteArray())
    println(decrypted)
}
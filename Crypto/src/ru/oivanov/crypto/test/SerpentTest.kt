package ru.oivanov.crypto.test

import ru.oivanov.crypto.algorithm.Serpent


@ExperimentalUnsignedTypes
fun main() {
    val message = "Hello world! This is my secret super message... I do not want to show it to you!"
    val key = Serpent.key("super secret key")


    val encrypted = ArrayList<Byte>()
    Serpent.encrypt(message.toByteArray().inputStream(), key) { it ->
        it.forEach { b ->
            encrypted.add(b)
        }
        print(it.joinToString("") { it.toUByte().toString(16) } + " ")
    }
    println()

    val decrypted = ArrayList<Byte>()
    Serpent.decrypt(encrypted.toByteArray().inputStream(), key) {
        it.forEach { b ->
            decrypted.add(b)
        }
    }

    println(String(decrypted.toByteArray()))
}
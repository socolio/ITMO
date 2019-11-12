package ru.oivanov.crypto.test

import ru.oivanov.crypto.algorithm.RC4
import java.io.File


fun main() {

    val bytesCount = 2

    val message = File("alice.txt").readLines().joinToString(" ")
    println("Message: $message\n")

    println("Enter key:")
    val key = readLine()!!

    val encrypted = ArrayList<Byte>()

    println()
    RC4.modifyStream(key, bytesCount, message.byteInputStream()){
        encrypted.add(it.toByte())
        print(it.toString(16) + " ")
    }
    println("\n")

    val decrypted = ArrayList<Byte>()
    RC4.modifyStream(key, bytesCount, encrypted.toByteArray().inputStream()){
        decrypted.add(it.toByte())
    }

    println(String(decrypted.toByteArray()))
}
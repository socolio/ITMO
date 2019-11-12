package ru.oivanov.crypto.test

import ru.oivanov.crypto.algorithm.Vigenere
import ru.oivanov.crypto.alphabet.ShiftedAlphabetGenerator
import ru.oivanov.crypto.analisis.KasiskiTest
import java.io.File


fun main() {

    val alphabetGenerator = ShiftedAlphabetGenerator()

    val message = File("alice.txt").readLines().joinToString(" ")
    println("Message: $message\n")

    val encrypted = Vigenere.encrypt(message, 5, alphabetGenerator)
    println("Encrypted: $encrypted\n")

    val possibleAlphabetsCounts = KasiskiTest.quessAlphabetsCount(encrypted, 20, 0.2f)
    println("Possible alphabets sizes: " + possibleAlphabetsCounts.joinToString(" "))
    println("Try guess: [enter size]")

    while (true){
        val size = readLine()!!.toInt()
        val (decrypted, key) = Vigenere.decrypt(encrypted, size, alphabetGenerator)
        println("Decrypted: $decrypted")
        println("Key: $key")
    }

}
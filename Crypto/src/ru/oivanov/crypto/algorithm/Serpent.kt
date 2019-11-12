package ru.oivanov.crypto.algorithm

import ru.oivanov.crypto.tool.PBox
import ru.oivanov.crypto.tool.SBox
import java.io.InputStream
import java.io.OutputStream
import java.io.PrintStream
import java.nio.ByteBuffer
import java.util.*


@ExperimentalUnsignedTypes
object Serpent {

    private val IP = PBox(intArrayOf(0, 32, 64, 96, 1, 33, 65, 97, 2, 34, 66, 98, 3, 35, 67, 99,
            4, 36, 68, 100, 5, 37, 69, 101, 6, 38, 70, 102, 7, 39, 71, 103,
            8, 40, 72, 104, 9, 41, 73, 105, 10, 42, 74, 106, 11, 43, 75, 107,
            12, 44, 76, 108, 13, 45, 77, 109, 14, 46, 78, 110, 15, 47, 79, 111,
            16, 48, 80, 112, 17, 49, 81, 113, 18, 50, 82, 114, 19, 51, 83, 115,
            20, 52, 84, 116, 21, 53, 85, 117, 22, 54, 86, 118, 23, 55, 87, 119,
            24, 56, 88, 120, 25, 57, 89, 121, 26, 58, 90, 122, 27, 59, 91, 123,
            28, 60, 92, 124, 29, 61, 93, 125, 30, 62, 94, 126, 31, 63, 95, 127))

    private val FP = PBox(intArrayOf(0, 4, 8, 12, 16, 20, 24, 28, 32, 36, 40, 44, 48, 52, 56, 60,
            64, 68, 72, 76, 80, 84, 88, 92, 96, 100, 104, 108, 112, 116, 120, 124,
            1, 5, 9, 13, 17, 21, 25, 29, 33, 37, 41, 45, 49, 53, 57, 61,
            65, 69, 73, 77, 81, 85, 89, 93, 97, 101, 105, 109, 113, 117, 121, 125,
            2, 6, 10, 14, 18, 22, 26, 30, 34, 38, 42, 46, 50, 54, 58, 62,
            66, 70, 74, 78, 82, 86, 90, 94, 98, 102, 106, 110, 114, 118, 122, 126,
            3, 7, 11, 15, 19, 23, 27, 31, 35, 39, 43, 47, 51, 55, 59, 63,
            67, 71, 75, 79, 83, 87, 91, 95, 99, 103, 107, 111, 115, 119, 123, 127))

    private val S = SBox(arrayOf(intArrayOf(3, 8, 15, 1, 10, 6, 5, 11, 14, 13, 4, 2, 7, 0, 9, 12),
            intArrayOf(15, 12, 2, 7, 9, 0, 5, 10, 1, 11, 14, 8, 6, 13, 3, 4),
            intArrayOf(8, 6, 7, 9, 3, 12, 10, 15, 13, 1, 14, 4, 0, 11, 5, 2),
            intArrayOf(0, 15, 11, 8, 12, 9, 6, 3, 13, 1, 2, 4, 10, 7, 5, 14),
            intArrayOf(1, 15, 8, 3, 12, 0, 11, 6, 2, 5, 4, 10, 9, 14, 7, 13),
            intArrayOf(15, 5, 2, 11, 4, 10, 9, 12, 0, 3, 14, 8, 13, 6, 7, 1),
            intArrayOf(7, 2, 12, 5, 8, 4, 6, 11, 14, 9, 1, 15, 13, 3, 10, 0),
            intArrayOf(1, 13, 15, 0, 14, 8, 2, 11, 7, 4, 12, 10, 9, 3, 5, 6)))


    private val DS = SBox(arrayOf(intArrayOf(13, 3, 11, 0, 10, 6, 5, 12, 1, 14, 4, 7, 15, 9, 8, 2),
            intArrayOf(5, 8, 2, 14, 15, 6, 12, 3, 11, 4, 7, 9, 1, 13, 10, 0),
            intArrayOf(12, 9, 15, 4, 11, 14, 1, 2, 0, 3, 6, 13, 5, 8, 10, 7),
            intArrayOf(0, 9, 10, 7, 11, 14, 6, 13, 3, 5, 12, 2, 4, 8, 15, 1),
            intArrayOf(5, 0, 8, 3, 10, 9, 7, 14, 2, 12, 11, 6, 4, 15, 13, 1),
            intArrayOf(8, 15, 2, 9, 4, 1, 13, 14, 11, 6, 5, 3, 7, 12, 10, 0),
            intArrayOf(15, 10, 1, 13, 5, 3, 6, 0, 4, 9, 14, 7, 2, 12, 8, 11),
            intArrayOf(3, 0, 6, 13, 9, 14, 15, 8, 5, 12, 11, 7, 10, 1, 4, 2)))

    private val ENCRYPT = PBox(intArrayOf(12, 13, 14, 15, 8, 9, 10, 11, 4, 5, 6, 7, 0, 1, 2, 3))
    private val DECRYPT = PBox(ENCRYPT.table.reversedArray())
    private val INVERTED = PBox(intArrayOf(3, 2, 1, 0, 7, 6, 5, 4, 11, 10, 9, 8, 15, 14, 13, 12))

    fun encrypt(input: InputStream, key: UByteArray, handler: (ByteArray) -> Unit) = use(input, key, handler, true)

    fun decrypt(input: InputStream, key: UByteArray, handler: (ByteArray) -> Unit) = use(input, key, handler, false)

    fun use(input: InputStream, key: UByteArray, handler: (ByteArray) -> Unit, encrypt: Boolean){
        val keys = generateKeys(key)

        while (input.available() != 0){
            val block = ByteArray(16)
            input.read(block)


            handler(if(encrypt) encrypt(block, keys) else decrypt(block, keys))
        }
    }

    fun encrypt(block: ByteArray, keys: Array<UByteArray>): ByteArray {
        var data = ENCRYPT.useForReorder(IP.use(block))

        for (i in 0 until 32) {
            applyKey(data, keys[i])
            data = S.useForSerpent(data, i)

            if (i != 31) {
                data = linearTransform(data, false, i == 0)
            } else {
                applyKey(data, keys[32])
            }
        }

        return INVERTED.useForReorder(FP.useReversed(data)).toByteArray()
    }

    fun decrypt(block: ByteArray, keys: Array<UByteArray>): ByteArray {
        var data = IP.use(DECRYPT.useForReorder(block))

        applyKey(data, keys[32])

        for (i in 31 downTo 0) {
            if (i != 31) {
                data = linearTransform(data, true, true)
            }

            data = DS.useForSerpent(data, i)
            applyKey(data, keys[i])
        }

        return INVERTED.useForReorder(FP.useReversed(data)).toByteArray()
    }

    private fun linearTransform(value: UByteArray, invert: Boolean, degub: Boolean): UByteArray {
        val buffer = ByteBuffer.wrap(FP.useReversed(value).toByteArray())

        var a = buffer.int
        var b = buffer.int
        var c = buffer.int
        var d = buffer.int
        buffer.clear()

        if (invert) {
            c = c.ushr(22) or (c shl 32 - 22)
            a = a.ushr(5) or (a shl 32 - 5)
            c = c xor d xor (b shl 7)
            a = a xor b xor d
            d = d.ushr(7) or (d shl 32 - 7)
            b = b.ushr(1) or (b shl 32 - 1)
            d = d xor c xor (a shl 3)
            b = b xor a xor c
            c = c.ushr(3) or (c shl 32 - 3)
            a = a.ushr(13) or (a shl 32 - 13)
        } else {
            a = a shl 13 or a.ushr(32 - 13)
            c = c shl 3 or c.ushr(32 - 3)
            b = b xor a xor c
            d = d xor c xor (a shl 3)
            b = b shl 1 or b.ushr(32 - 1)
            d = d shl 7 or d.ushr(32 - 7)
            a = a xor b xor d
            c = c xor d xor (b shl 7)
            a = a shl 5 or a.ushr(32 - 5)
            c = c shl 22 or c.ushr(32 - 22)
        }
        return IP.use(buffer.putInt(a).putInt(b).putInt(c).putInt(d).array())
    }

    private fun applyKey(block: UByteArray, key: UByteArray) {
        for (j in block.indices) {
            block[j] = block[j] xor key[j]
        }
    }

    fun generateKeys(k: UByteArray): Array<UByteArray> {
        val key = if (k.size != 32) {
            UByteArray(32) {
                if (it < k.size) {
                    k[it]
                } else {
                    (if (it == k.size) 0x80 else 0).toUByte()
                }
            }
        } else k

        val rawKeys = IntArray(140)

        for (i in rawKeys.indices) {
            rawKeys[i] = if (i < 8) {
                ByteBuffer.wrap(byteArrayOf(key[4 * i].toByte(), key[4 * i + 1].toByte(), key[4 * i + 2].toByte(), key[4 * i + 3].toByte())).int
            } else {
                val phi = -0x61c88647
                val v = rawKeys[i - 8] xor rawKeys[i - 5] xor rawKeys[i - 3] xor rawKeys[i - 1] xor i - 8 xor phi

                v shl 11 or v.ushr(21)
            }
        }

        return Array<UByteArray>(33) { round ->
            val a = rawKeys[4 * round + 8]
            val b = rawKeys[4 * round + 9]
            val c = rawKeys[4 * round + 10]
            val d = rawKeys[4 * round + 11]

            val arr = UByteArray(16)

            for(j in 0 until 32 step 2){
                arr[j / 2] = (a.ushr(j) and 0x01 or (
                        b.ushr(j) and 0x01 shl 1) or (
                        c.ushr(j) and 0x01 shl 2) or (
                        d.ushr(j) and 0x01 shl 3) or (
                        a.ushr(j + 1) and 0x01 shl 4) or (
                        b.ushr(j + 1) and 0x01 shl 5) or (
                        c.ushr(j + 1) and 0x01 shl 6) or (
                        d.ushr(j + 1) and 0x01 shl 7)).toUByte()
            }

            val boxed = S.useForSerpent(arr, ((3 - round) % 8 + 8) % 8)
            val result = UByteArray(16)

            for (i in 3 downTo 0) {
                for (j in 0..3) {
                    result[3 - i] = result[3 - i] or (boxed[i * 4 + j].toInt() and 0x01 shl j * 2 or (boxed[i * 4 + j].toInt().ushr(4) and 0x01 shl j * 2 + 1)).toUByte()
                    result[7 - i] = result[7 - i] or (boxed[i * 4 + j].toInt().ushr(1) and 0x01 shl j * 2 or (boxed[i * 4 + j].toInt().ushr(5) and 0x01 shl j * 2 + 1)).toUByte()
                    result[11 - i] = result[11 - i] or (boxed[i * 4 + j].toInt().ushr(2) and 0x01 shl j * 2 or (boxed[i * 4 + j].toInt().ushr(6) and 0x01 shl j * 2 + 1)).toUByte()
                    result[15 - i] = result[15 - i] or (boxed[i * 4 + j].toInt().ushr(3) and 0x01 shl j * 2 or (boxed[i * 4 + j].toInt().ushr(7) and 0x01 shl j * 2 + 1)).toUByte()
                }
            }

            IP.use(result)
        }
    }

    fun key(key : String) : UByteArray{
        require(!(key.length != 16 && key.length != 24 && key.length != 32)) { "Invalid key length (might be 16/24/32)" }

        return UByteArray(key.length){
            key[it].toInt().toUByte()
        }
    }
}
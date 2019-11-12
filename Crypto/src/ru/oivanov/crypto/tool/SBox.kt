package ru.oivanov.crypto.tool

import ru.oivanov.crypto.util.getBit


class SBox(val table: Array<IntArray>) {

    @ExperimentalUnsignedTypes
    fun useForDES(value: UInt) : UInt {
        val row = (value.getBit(5) shl 1) or value.getBit(0)
        val column = (value shr 1) and 0b1111U

        return table[row.toInt()][column.toInt()].toUInt()
    }

    @ExperimentalUnsignedTypes
    fun useForSerpent(value: UByteArray, round: Int) : UByteArray{
        val box = table[round % 8]

        return UByteArray(value.size){
            val byte = value[it].toInt()
            val a = byte.ushr(4)
            val b = byte and 0x0F

            (box[a] shl 4 xor box[b]).toUByte()
        }
    }
}
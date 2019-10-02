package ru.oivanov.crypto.tool

import ru.oivanov.crypto.getBit


class SBox(val table: Array<IntArray>) {

    @ExperimentalUnsignedTypes
    fun use(value: UInt) : UInt {
        val row = (value.getBit(5) shl 1) or value.getBit(0)
        val column = (value shr 1) and 0b1111U

        return table[row.toInt()][column.toInt()].toUInt()
    }
}
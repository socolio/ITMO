package ru.oivanov.crypto.tool

import ru.oivanov.crypto.getBit
import ru.oivanov.crypto.setBit


class PBox(val table: IntArray){

    @ExperimentalUnsignedTypes
    fun use(value: ULong) : ULong{
        var result = 0UL
        for (i in table.indices){
            result = result.setBit(i, value.getBit(table[i] - 1))
        }
        return result
    }
}
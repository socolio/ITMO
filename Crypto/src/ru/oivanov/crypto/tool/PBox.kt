package ru.oivanov.crypto.tool

import ru.oivanov.crypto.util.getBit
import ru.oivanov.crypto.util.setBit
import kotlin.experimental.and
import kotlin.experimental.or


class PBox(val table: IntArray){

    @ExperimentalUnsignedTypes
    fun use(value: ULong) : ULong{
        var result = 0UL
        for (i in table.indices){
            result = result.setBit(i, value.getBit(table[i] - 1))
        }
        return result
    }

    @ExperimentalUnsignedTypes
    fun useForReorder(value: UByteArray) : UByteArray{
        return UByteArray(value.size){
            value[table[it]]
        }
    }

    @ExperimentalUnsignedTypes
    fun useForReorder(value: ByteArray) : ByteArray{
        return ByteArray(value.size){
            value[table[it]]
        }
    }

    @ExperimentalUnsignedTypes
    fun use(value: UByteArray) : UByteArray{
        val result = UByteArray(value.size)

        for (i in table.indices){
            val a = table[i]

            result[15 - i / 8] = result[15 - i / 8].setBit(i % 8, value[a / 8].getBit(a % 8))
        }

        return result
    }

    @ExperimentalUnsignedTypes
    fun useReversed(value: UByteArray) : UByteArray{
        val result = UByteArray(value.size)

        for (i in table.indices){
            val a = table[i]

            result[i / 8] = result[i / 8].setBit(i % 8, value[15 - a / 8].getBit(a % 8))
        }

        return result
    }

    @ExperimentalUnsignedTypes
    fun use(value: ByteArray) : UByteArray{
        val result = UByteArray(value.size)

        for (i in table.indices){
            val a = table[i]

            result[15 - i / 8] = result[15 - i / 8].setBit(i % 8, value[a / 8].getBit(a % 8))
        }

        return result
    }
}
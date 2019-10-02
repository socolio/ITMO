package ru.oivanov.crypto.tool


@ExperimentalUnsignedTypes
object FeistelNetwork {

    fun use(value: ULong, encrypt: Boolean, function: (ULong) -> ULong) : ULong{
        val right = value and 0xFFFFFFFFU
        val left = value shr 32

        return if(encrypt){
            (right shl 32) or (left xor function(right))
        }else{
            ((right xor function(left)) shl 32) or left
        }
    }
}
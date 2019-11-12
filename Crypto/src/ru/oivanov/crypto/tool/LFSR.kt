package ru.oivanov.crypto.tool

import ru.oivanov.crypto.util.getBit


class LFSR(private val length: Int, private val syncBit: Int, private val feedbackBits: Array<Int>) {

    private var reg = 0UL

    fun reset(){
        reg = 0UL
    }

    fun getSyncBit() = reg.getBit(syncBit)

    fun shiftWith(bit: Int) : Int {
        reg = (reg shl 1) or (bit and 0x1).toULong()
        return reg.getBit(length)
    }

    fun shiftXor(x: Int) : Int {
        var bit = -1
        feedbackBits.forEach {
            bit = if(bit == -1){
                reg.getBit(it)
            }else{
                (bit xor reg.getBit(it)) and 0x1
            }
        }
        return shiftWith(bit xor x)
    }

    fun shift()  : Int {
        var bit = -1
        feedbackBits.forEach {
            bit = if(bit == -1){
                reg.getBit(it)
            }else{
                (bit xor reg.getBit(it)) and 0x1
            }
        }
        return shiftWith(bit)
    }
}
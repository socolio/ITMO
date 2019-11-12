package ru.oivanov.crypto.util


@ExperimentalUnsignedTypes
fun ULong.setBit(i: Int, v: Int): ULong {
    return if (v == 1) this or (1UL shl i) else ((this or (1UL shl i)) xor (1UL shl i))
}

@ExperimentalUnsignedTypes
fun UByte.setBit(i: Int, v: Int): UByte {
    return if (v == 1) this or (1 shl i).toUByte() else ((this or (1 shl i).toUByte()) xor (1 shl i).toUByte())
}

@ExperimentalUnsignedTypes
fun ULong.getBit(i: Int): Int {
    return ((this shr i) and 1UL).toInt()
}

@ExperimentalUnsignedTypes
fun UByte.getBit(i: Int): Int {
    return ((this.toUInt() shr i) and 1U).toInt()
}

@ExperimentalUnsignedTypes
fun Byte.getBit(i: Int): Int {
    return ((this.toUInt() shr i) and 1U).toInt()
}

@ExperimentalUnsignedTypes
fun UInt.getBit(i: Int): UInt {
    return (this shr i) and 1U
}

@ExperimentalUnsignedTypes
fun Int.getBit(i: Int): Int {
    return (this shr i) and 1
}

@ExperimentalUnsignedTypes
fun ByteArray.toULongArray(): ULongArray {
    return ULongArray(if (this.size % 8 == 0) this.size / 8 else this.size / 8 + 1) { i ->
        Utils.bytesToLong(ByteArray(8) { j ->
            val index = i * 8 + j
            if (index >= this.size) 0 else this[index]
        }).toULong()
    }
}

@ExperimentalUnsignedTypes
fun ULongArray.toByteArray() : ByteArray {
    val bytes = ByteArray(this.size * 8)

    this.forEachIndexed { index, l ->
        val b = Utils.longToBytes(l.toLong())
        for (i in 0 until 8){
            bytes[index * 8 + i] = b[i]
        }
    }

    return bytes
}

@ExperimentalUnsignedTypes
fun UByteArray.joinToHEXString() = this.joinToString("") { if(it <= 0x0F.toUByte()) "0${it.toString(16)}" else it.toString(16) }

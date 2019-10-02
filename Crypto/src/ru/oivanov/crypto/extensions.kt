package ru.oivanov.crypto


@ExperimentalUnsignedTypes
fun ULong.setBit(i: Int, v: Int): ULong {
    return if (v == 1) this or (1UL shl i) else ((this or (1UL shl i)) xor (1UL shl i))
}

@ExperimentalUnsignedTypes
fun ULong.getBit(i: Int): Int {
    return ((this shr i) and 1UL).toInt()
}

@ExperimentalUnsignedTypes
fun UInt.getBit(i: Int): UInt {
    return (this shr i) and 1U
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

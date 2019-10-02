package ru.oivanov.crypto.analisis


object KasiskiTest {

    fun quessAlphabetsCount(text: String, max: Int, textPart: Float) : List<Int> {
        val text = text.filter { it in 'a'..'z' }

        val map = HashMap<Int, Int>()

        for (i in 0 until (text.length * textPart - 2).toInt()){
            for (j in i + 2 until (text.length * textPart - 2).toInt()){
                if(text.regionMatches(i, text, j, 3)){
                    val d = j - i
                    for (f in 2..max){
                        if(d % f == 0){
                            map[f] = map.getOrDefault(f, 0) + 1
                        }
                    }
                }
            }
        }

        val sizes = ArrayList<Int>()
        val sorted = map.entries.sortedByDescending { it.value }

        if(sorted.isEmpty()){
            return listOf(1)
        }

        val prev = sorted[0].value
        sorted.forEach {
            if(prev * 0.8 <= it.value){
                sizes += it.key
            }
        }

        sizes.sort()
        return sizes
    }
}

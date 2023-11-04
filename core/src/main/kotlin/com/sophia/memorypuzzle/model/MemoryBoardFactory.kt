package com.sophia.memorypuzzle.model

import kotlin.random.Random

class MemoryBoardFactory {
    val random = Random(99)

    fun createNewBoard(width: Int, height: Int): MemoryBoard {
        val numberOfCards = width*height
        if (numberOfCards%2 != 0) throw Error("Board must have even size")

        val ids = mutableListOf<Int>()
        for (i in 0 until width*height/2){
            ids.add(i)
            ids.add(i)
        }

        val cards : Array<Array<MemoryCard?>> = Array(width){x ->
            Array(height){y ->
                val id = ids.random(random)
                ids.remove(id)
                MemoryCard(x, y, id)
            }
        }

        return MemoryBoard(cards)
    }

}

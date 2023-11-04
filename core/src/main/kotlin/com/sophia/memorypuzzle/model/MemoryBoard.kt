package com.sophia.memorypuzzle.model

class MemoryBoard(val cards : Array<Array<MemoryCard?>>) {

    fun remove(first: MemoryCard) {
        cards[first.x][first.y] = null
    }

}

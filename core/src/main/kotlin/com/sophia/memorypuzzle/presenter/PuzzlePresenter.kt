package com.sophia.memorypuzzle.presenter

import com.sophia.memorypuzzle.model.MemoryBoard
import com.sophia.memorypuzzle.model.MemoryCard
import com.sophia.memorypuzzle.model.MemoryPuzzleGame

class PuzzlePresenter(val puzzleScreen: MainMVP.RequiredViewOps) :
    MainMVP.PresenterOps, MainMVP.RequiredPresenterOps {

    val memoryPuzzleGame = MemoryPuzzleGame(this)

    override fun onNewGame() {
        memoryPuzzleGame.createNewGame()
    }

    override fun onClickCard(memoryCard: MemoryCard) {
        memoryPuzzleGame.onClickCard(memoryCard)
    }

    override fun checkMatch() {
        memoryPuzzleGame.checkMatch()
    }

    override fun onMemoryBoardChanged(board: MemoryBoard) {
        puzzleScreen.showBoard(board)
    }

    override fun onSelectedCard(memoryCard: MemoryCard) {
        puzzleScreen.showAnimationSelection(memoryCard.x, memoryCard.y)
    }

    override fun onHideCard(value: MemoryCard) {
        puzzleScreen.hideCard(value.x, value.y)
    }

    override fun onCardRemoved(memoryCard: MemoryCard) {
        puzzleScreen.removeCard(memoryCard.x, memoryCard.y)
    }

}

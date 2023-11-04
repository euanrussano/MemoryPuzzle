package com.sophia.memorypuzzle.presenter

import com.sophia.memorypuzzle.model.MemoryBoard
import com.sophia.memorypuzzle.model.MemoryCard


interface MainMVP {
    /**
     * View mandatory methods. Available to Presenter
     * Presenter -> View
     */
    interface RequiredViewOps {
        fun showBoard(board: MemoryBoard)
        fun showAnimationSelection(x: Int, y: Int)
        fun hideCard(x: Int, y: Int)
        fun removeCard(x: Int, y: Int)
    }

    /**
     * Operations offered from Presenter to View
     * View -> Presenter
     */
    interface PresenterOps {
        fun onNewGame()
        fun onClickCard(memoryCard: MemoryCard)
        fun checkMatch()
    }

    /**
     * Operations offered from Presenter to Model
     * Model -> Presenter
     */
    interface RequiredPresenterOps {
        fun onMemoryBoardChanged(value: MemoryBoard)
        fun onSelectedCard(value: MemoryCard)
        fun onHideCard(value: MemoryCard)
        fun onCardRemoved(memoryCard: MemoryCard)

    }

    /**
     * Model operations offered to Presenter
     * Presenter -> Model
     */
    interface ModelOps {
        fun createNewGame()
        fun onClickCard(memoryCard: MemoryCard)
        fun checkMatch()
    }


}

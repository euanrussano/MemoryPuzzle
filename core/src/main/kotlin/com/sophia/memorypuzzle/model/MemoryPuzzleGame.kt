package com.sophia.memorypuzzle.model

import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.sophia.memorypuzzle.presenter.MainMVP

class MemoryPuzzleGame(val presenterOps: MainMVP.RequiredPresenterOps) : MainMVP.ModelOps {

    val memoryBoardFactory = MemoryBoardFactory()
    private var memoryBoard: MemoryBoard? = null
        set(value) {
            field = value
            if (value != null){
                presenterOps.onMemoryBoardChanged(value)
            }
        }

    private var firstSelection : MemoryCard? = null
        set(value) {
            if (value == null && field != null) presenterOps.onHideCard(field!!)
            field = value
            value?.let { presenterOps.onSelectedCard(value) }
        }
    private var secondSelection : MemoryCard? = null
        set(value) {
            if (value == null && field != null) presenterOps.onHideCard(field!!)
            field = value
            value?.let { presenterOps.onSelectedCard(value) }

        }

    override fun createNewGame() {
        memoryBoard = memoryBoardFactory.createNewBoard(4,4)
    }

    override fun onClickCard(memoryCard: MemoryCard) {
        if (firstSelection != null && secondSelection != null) return
        if (firstSelection == memoryCard || secondSelection == memoryCard) {
            return
        }else if (firstSelection == null){
            firstSelection = memoryCard
        } else{
            secondSelection = memoryCard
        }
    }

    override fun checkMatch() {
        firstSelection?.let {first ->
            secondSelection?.let { second ->
                if (first.id == second.id){
                    println("match!")
                    memoryBoard!!.remove(first)
                    memoryBoard!!.remove(second)
                    presenterOps.onCardRemoved(first)
                    presenterOps.onCardRemoved(second)
                } else {
                    println("not match")

                }
                firstSelection = null
                secondSelection = null

            }
        }
    }

}

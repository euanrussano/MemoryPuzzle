package com.sophia.memorypuzzle

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.Texture.TextureFilter.Linear
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.sophia.memorypuzzle.model.MemoryPuzzleGame
import com.sophia.memorypuzzle.screen.PuzzleScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.app.clearScreen
import ktx.assets.disposeSafely
import ktx.assets.toInternalFile
import ktx.graphics.use
import ktx.scene2d.Scene2DSkin

class MemoryPuzzle : Game() {

    override fun create() {
        Scene2DSkin.defaultSkin = Skin("ui/uiskin.json".toInternalFile())

        setScreen(PuzzleScreen(this))
    }
}

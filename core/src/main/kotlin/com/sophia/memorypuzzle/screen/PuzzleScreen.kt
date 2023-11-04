package com.sophia.memorypuzzle.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.sophia.memorypuzzle.MemoryPuzzle
import com.sophia.memorypuzzle.model.MemoryBoard
import com.sophia.memorypuzzle.model.MemoryCard
import com.sophia.memorypuzzle.model.MemoryPuzzleGame
import com.sophia.memorypuzzle.presenter.MainMVP
import com.sophia.memorypuzzle.presenter.PuzzlePresenter
import ktx.actors.centerPosition
import ktx.actors.onClick
import ktx.assets.disposeSafely
import ktx.scene2d.*


class PuzzleScreen(val game: MemoryPuzzle) : Screen, MainMVP.RequiredViewOps {

    private val actorsWithAnimations = mutableListOf<Actor>()
    private lateinit var menuWindow: KWindow

    private val mainStage = Stage(ExtendViewport(3f, 2f))
    private val uiStage = Stage(ExtendViewport(400f, 400f))

    private lateinit var hideTexture: Texture

    private lateinit var presenter : MainMVP.PresenterOps

    val shapes = mutableListOf(
        generateShape(Color.RED, "circle"),
        generateShape(Color.GREEN, "circle"),
        generateShape(Color.YELLOW, "circle"),
        generateShape(Color.DARK_GRAY, "circle"),
        generateShape(Color.LIGHT_GRAY, "circle"),
        generateShape(Color.SALMON, "circle"),
        generateShape(Color.FIREBRICK, "circle"),
        generateShape(Color.FOREST, "circle"),
        generateShape(Color.BROWN, "circle"),
        generateShape(Color.CHARTREUSE, "circle"),
        generateShape(Color.CYAN, "circle"),
        generateShape(Color.BLACK, "circle"),
    )

    override fun show() {
        Pixmap(64, 64, Pixmap.Format.RGBA8888).apply {
            this.setColor(Color.WHITE)
            fill()
            hideTexture = Texture(this)
            this.dispose()
        }

        uiStage.actors {
            table {
                setFillParent(true)
                table {
                    background = skin.newDrawable("white", Color(Color.DARK_GRAY).apply { a = 0.5f })
                    it.growX()
                    this.left().pad(10f)
                    debug()
                    textButton("Menu"){
                        this.pad(10f)
                        onClick {
                            uiStage.addActor(menuWindow)
                        }
                    }
                }
                row()
                table {
                    it.grow()
                }
            }
        }

        menuWindow = scene2d.window("Menu"){
            isModal = true
            isMovable = true
            this.padTop(20f)
            this.defaults().pad(10f)
            titleTable.debug()
            titleTable.add(textButton("X"){
                it.right()
                this.pad(10f)
                onClick {
                    this@window.remove()
                }
            })
            row()
            textButton("New Game"){
                this.pad(10f)
                onClick {
                    presenter.onNewGame()
                    this@window.remove()
                }
            }
            row()
            textButton("Exit"){
                this.pad(10f)
                onClick {
                    Gdx.app.exit()
                }
            }
            pack()
        }
        uiStage.addActor(menuWindow)
        menuWindow.centerPosition(uiStage.width, uiStage.height)


        val im = InputMultiplexer()
        im.addProcessor(uiStage)
        im.addProcessor(mainStage)
        Gdx.input.inputProcessor = im

        presenter = PuzzlePresenter(this)
    }

    private fun generateShape(color: Color, type: String): Texture {
        val texture : Texture
        Pixmap(64, 64, Pixmap.Format.RGBA8888).apply {
            this.setColor(color)
            if (type == "circle"){
                fillCircle(32, 32, 16)
            } else {
                fill()
            }
            texture = Texture(this)
            this.dispose()
        }

        return texture
    }

    private fun createBoardActor(memoryCard: MemoryCard): Actor {
        val actor = Group()
        actor.userObject = memoryCard
        actor.setSize(0.8f, 0.8f)
        val icon = shapes[memoryCard.id]
        actor.addActor(Image(icon).apply { name="icon"; setFillParent(true);touchable=Touchable.disabled })
        actor.addActor(Image(hideTexture).apply { name = "hide"; setFillParent(true);touchable=Touchable.disabled })
        actor.setPosition(memoryCard.x.toFloat(), memoryCard.y.toFloat())
        actor.onClick{
            presenter.onClickCard(memoryCard)
        }
        return actor
    }

    override fun render(delta: Float) {
        val iterator = actorsWithAnimations.iterator()
        while(iterator.hasNext()){
            val actor = iterator.next()
            if (!actor.hasActions()){
                iterator.remove()
            }
        }
        if (actorsWithAnimations.isEmpty()){
            presenter.checkMatch()
        }

        ScreenUtils.clear(Color.NAVY)

        mainStage.viewport.apply()
        mainStage.act(delta)
        mainStage.draw()

        uiStage.viewport.apply()
        uiStage.act(delta)
        uiStage.draw()
    }

    override fun resize(width: Int, height: Int) {
        mainStage.viewport.update(width, height)
        uiStage.viewport.update(width, height)
    }

    override fun pause() {
        TODO("Not yet implemented")
    }

    override fun resume() {
        TODO("Not yet implemented")
    }

    override fun hide() {
        TODO("Not yet implemented")
    }

    override fun dispose() {
        mainStage.disposeSafely()
        uiStage.disposeSafely()
    }

    override fun showBoard(board: MemoryBoard) {
        println("${board.cards.size.toFloat()}, ${board.cards[0].size.toFloat()}")
        mainStage.viewport = ExtendViewport(board.cards.size.toFloat(), board.cards[0].size.toFloat())
        mainStage.viewport.update(Gdx.graphics.width, Gdx.graphics.height, true)
        mainStage.root.clearChildren()
        val mainBoard = Group()
        board.cards.flatten().forEach { memoryCard ->
            if (memoryCard == null) return@forEach
            val actor = createBoardActor(memoryCard)
            mainBoard.addActor(actor)
        }
        mainBoard.children.forEach {
            val hide = (it as Group).findActor<Image>("hide")
            hide.addAction(Actions.sequence(
                Actions.visible(false),
                Actions.alpha(0f),
                Actions.delay(2f),
                Actions.visible(true),
                Actions.fadeIn(1f),
            ))
        }
        mainBoard.addAction(Actions.sequence(
            Actions.touchable(Touchable.disabled),
            Actions.delay(3.5f),
            Actions.touchable(Touchable.enabled),
        ))

        mainStage.addActor(mainBoard)
    }

    override fun showAnimationSelection(x: Int, y: Int) {
        val cardGroup  = mainStage.hit(x.toFloat(), y.toFloat(), true) as Group
        val hideActor = cardGroup.findActor<Actor>("hide")
        // block input in beginning of animation
        hideActor.addAction(Actions.sequence(Actions.fadeOut(1f), Actions.visible(false), Actions.delay(0.5f)))
        actorsWithAnimations.add(hideActor)
    }

    override fun hideCard(x: Int, y: Int) {
        val cardGroup  = mainStage.hit(x.toFloat(), y.toFloat(), true) as? Group ?: return
        val hideActor = cardGroup.findActor<Actor>("hide")
        // block input in beginning of animation
        hideActor.addAction(Actions.sequence(Actions.visible(true), Actions.fadeIn(1f), Actions.delay(0.5f)))
        actorsWithAnimations.add(hideActor)
    }

    override fun removeCard(x: Int, y: Int) {
        val cardGroup  = mainStage.hit(x.toFloat(), y.toFloat(), true) as? Group ?: return
        cardGroup.remove()
    }


}

package xyz.jmullin.hangman.entity

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import xyz.jmullin.drifter.animation.delay
import xyz.jmullin.drifter.animation.event
import xyz.jmullin.drifter.animation.tween
import xyz.jmullin.drifter.entity.Entity2D
import xyz.jmullin.drifter.extensions.*
import xyz.jmullin.drifter.rendering.RenderStage
import xyz.jmullin.drifter.rendering.sprite
import xyz.jmullin.drifter.rendering.string
import xyz.jmullin.hangman.Assets
import xyz.jmullin.hangman.Hangman
import xyz.jmullin.hangman.Stage
import xyz.jmullin.hangman.game.Puzzle

/**
 * Puzzle visualizer. Displays all players and their current status.
 */
class PuzzleDisplay(var puzzle: Puzzle? = null) : Entity2D() {
    companion object {
        val TurnDelay = 200L
        val RefreshDelay = 1000L
    }

    var round = 0

    private val players: List<Puzzle.PlayerState>
        get() = puzzle?.playerStatuses ?: emptyList()

    private var scores = emptyMap<String, Int>()
    private var toasts = emptyMap<String, Int>()
    private var playerY = mutableMapOf<String, Float>()
    private val displayScores = mutableMapOf<String, Float>()

    private val glyphLayout = GlyphLayout()

    private var puzzleAlpha = 1f
    private var toastAlpha = 0f

    private var initYs = false

    fun applyScores(newScores: Map<String, Int>) {
        delay(0.5f * Hangman.delayMultiplier) {} then tween(1.5f) { a ->
            toastAlpha = 1f-a
        } go(this)

        delay(0.75f * Hangman.delayMultiplier) {} then tween(0.25f) { a ->
            puzzleAlpha = 1f-a
        } then event {
            scores = newScores.toMap()
        } then delay(0.25f * Hangman.delayMultiplier) {} then tween(0.5f * Hangman.delayMultiplier) { a ->
            if(round < Hangman.NumPuzzles) puzzleAlpha = a
        } go(this)
    }

    override fun update(delta: Float) {
        scores.forEach { player, score ->
            displayScores.compute(player, { _, current ->
                val display = current ?: 0f
                if(display < score) minOf(score.toFloat(), display + delta*10f) else display
            })
        }

        Hangman.delayMultiplier = when {
            Gdx.input.isKeyPressed(Input.Keys.SPACE) -> 0.15f
            else -> 1f
        }

        super.update(delta)
    }

    override fun render(stage: RenderStage) {
        val maxNameWidth: Float = players.map {
            glyphLayout.setText(Assets.uiFont, it.implementation.name)
            glyphLayout.width
        }.max() ?: 0f

        val scoreWidth = 128f

        glyphLayout.setText(Assets.puzzleFont, puzzle?.solution ?: "")
        val puzzleWidth = glyphLayout.width

        stage.draw(Stage.Ui) {
            val dY = gameH() / (players.size+1)
            val offsetY = gameH()/2f - (dY*players.size.toFloat())/2f

            Assets.titleFont.color = Color.WHITE
            string("NERDERY HANGMAN CHALLENGE", V2(gameW()/2f, gameH()), Assets.titleFont, V2(0f, -1f))

            Assets.uiFont.color = Color.WHITE
            val roundMessage = if(round < Hangman.NumPuzzles) {
                "Round ${round+1}/${Hangman.NumPuzzles}"
            } else {
                "Final"
            }
            string(roundMessage, V2(8f, gameH()-8f), Assets.uiFont, V2(1f, -1f))

            players.sortedBy { scores[it.implementation.name] ?: 0 }
            .forEachIndexed { playerI, player ->
                val name = player.implementation.name

                val targetY = offsetY + dY*playerI
                val y = if(!initYs) {
                    playerY.put(name, targetY)
                    targetY
                } else {
                    playerY.compute(name, { _, currentY -> (currentY ?: 0f) + (targetY - (currentY ?: 0f))/10f }) ?: 0f
                }

                val score = displayScores[name]?.toInt() ?: 0
                Assets.uiFont.color = when {
                    round < Hangman.NumPuzzles -> Color.WHITE
                    playerI == players.size-1 -> {
                        Assets.uiFont.color = Color.YELLOW
                        string("WINNER", V2(scoreWidth + maxNameWidth + 48f, y), Assets.uiFont, V2(1f, 0f))
                        Color.YELLOW
                    }
                    else -> Color.WHITE
                }
                string(score.toString(), V2(8f, y), Assets.uiFont, V2(1f, 0f))
                string(name, V2(scoreWidth, y), Assets.uiFont, V2(1f, 0f))

                val toast = toasts[name] ?: 0
                Assets.uiFont.color = when {
                    toast > 0 -> Color.GREEN
                    else -> Color.RED
                }.alpha(toastAlpha)
                string("+$toast", V2(scoreWidth-8f, y), Assets.uiFont, V2(-1f, 0f))


                Assets.puzzleFont.color = when {
                    player.strikes == Puzzle.MaxStrikes -> Color.RED
                    player.puzzle == puzzle?.solution -> Color.GREEN
                    else -> Color.WHITE
                }.alpha(puzzleAlpha)
                string(player.puzzle, V2(scoreWidth + maxNameWidth + 48f, y), Assets.puzzleFont, V2(1f, 0f))

                Assets.puzzleFont.color = Color.WHITE.alpha(puzzleAlpha)
                player.misses.forEachIndexed { strikeI, miss ->
                    string(miss.toString(), V2(scoreWidth + maxNameWidth + puzzleWidth + 96f + 30f*strikeI, y), Assets.puzzleFont, V2(1f, 0f))
                    Assets.strike.color = Color.WHITE.alpha(puzzleAlpha)
                    sprite(Assets.strike, V2(scoreWidth + maxNameWidth + puzzleWidth + 96f + 30f*strikeI, y).rect(V2(16f), V2(2.5f, 1.5f)))
                }
            }

            initYs = true
        }

        super.render(stage)
    }
}
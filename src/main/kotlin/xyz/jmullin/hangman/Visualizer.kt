package xyz.jmullin.hangman

import xyz.jmullin.drifter.application.DrifterScreen
import xyz.jmullin.drifter.extensions.*
import xyz.jmullin.hangman.Stage.Ui
import xyz.jmullin.hangman.entity.PuzzleDisplay
import xyz.jmullin.hangman.entity.QuitButton
import xyz.jmullin.hangman.entity.SpeedButton
import xyz.jmullin.hangman.game.Playback

/**
 * LibGDX screen definition for rendering the visualizer.
 */
object Visualizer : DrifterScreen() {
    var puzzleDisplay = PuzzleDisplay()

    val ui = newLayer2D(2, Hangman.Size, false, Ui) {
        add(puzzleDisplay)
        add(QuitButton())

        listOf(
            Assets.slow to Playback.Slow,
            Assets.play to Playback.Play,
            Assets.fastForward to Playback.FastForward,
            Assets.tripleFast to Playback.TripleFast
        ).forEachIndexed { i, (icon, mode) ->
            add(SpeedButton(mode) { icon }).apply {
                position.set(V2(155f+36f*i, gameH()-9f))
            }
        }

        cameraPos = gameSize()/2f

        if(drifter().devMode) {

        }
    }
    }
package xyz.jmullin.hangman

import xyz.jmullin.drifter.application.DrifterScreen
import xyz.jmullin.drifter.extensions.*
import xyz.jmullin.hangman.Stage.Ui
import xyz.jmullin.hangman.entity.PuzzleDisplay

/**
 * LibGDX screen definition for rendering the visualizer.
 */
object Visualizer : DrifterScreen() {
    var puzzleDisplay = PuzzleDisplay()

    val ui = newLayer2D(2, Hangman.Size, false, Ui) {
        add(puzzleDisplay)

        cameraPos = gameSize()/2f

        if(drifter().devMode) {

        }
    }
}
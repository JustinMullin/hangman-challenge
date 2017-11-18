package xyz.jmullin.hangman

import xyz.jmullin.drifter.assets.DrifterAssets

/**
 * Loads game assets and provides handles to them for simplified access.
 */
@Suppress("UNUSED")
object Assets : DrifterAssets("hangman") {
    val titleFont by font("hangman")
    val puzzleFont by font("upheaval")
    val uiFont by font("kenyanCoffee")

    val strike by sprite

    val slow by sprite
    val play by sprite
    val fastForward by sprite
    val tripleFast by sprite
}
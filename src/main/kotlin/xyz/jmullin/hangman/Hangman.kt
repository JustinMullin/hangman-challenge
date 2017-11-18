package xyz.jmullin.hangman

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import org.reflections.Reflections
import xyz.jmullin.drifter.application.DrifterGame
import xyz.jmullin.drifter.extensions.V2
import xyz.jmullin.drifter.extensions.xI
import xyz.jmullin.drifter.extensions.yI
import xyz.jmullin.hangman.game.HangmanBot
import xyz.jmullin.hangman.game.Playback
import xyz.jmullin.hangman.game.Tournament

/**
 * Main entry point to the game. Starts up a tournament, and optionally a visualizer.
 */
fun main(args: Array<String>) {
    val reflections = Reflections("xyz.jmullin.hangman.bot")
    val bots = reflections.getSubTypesOf(HangmanBot::class.java).mapNotNull {
        if(!it.isInterface && !it.`package`.name.contains("example")) {
            it.newInstance()
        } else null
    }

    val tournament = Tournament(Hangman.NumPuzzles, bots)

    if(!args.contains("headless")) {
        Hangman.visualize = true

        val config = Lwjgl3ApplicationConfiguration()

        config.setTitle(Hangman.Name)
        config.setWindowedMode(Hangman.Size.xI, Hangman.Size.yI)
        config.setResizable(false)

        config.useOpenGL3(true, 3, 3)

        config.useVsync(true)
        config.setIdleFPS(60)

        Lwjgl3Application(Hangman(tournament), config)
    } else {
        runBlocking { runTournament(tournament) }
    }
}

class Hangman(private val tournament: Tournament) : DrifterGame(Name, Assets) {
    companion object {
        val Name = "hangman"
        val Size = V2(1024f, 768f)
        val MaxTurnTime = 1000L
        val NumPuzzles = 50

        var visualize = false

        var playbackMode: Playback = Playback.Play
        val delayMultiplier: Float
            get() = playbackMode.delayMultiplier
    }

    override fun create() {
        super.create()

        launch { runTournament(tournament) }
        setScreen(Visualizer)
    }
}

/**
 * Run a tournament with the specified number of puzzles, and print the final results.
 */
suspend fun runTournament(tournament: Tournament) {
    val scores = tournament.start()
    println("\nFinal scores:")
    println(scores.toList().sortedBy {
        -it.second
    }.joinToString("\n") {
        "\t${it.first}: ${it.second}"
    })
}
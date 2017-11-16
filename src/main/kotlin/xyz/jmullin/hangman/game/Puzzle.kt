package xyz.jmullin.hangman.game

import kotlinx.coroutines.experimental.*
import xyz.jmullin.hangman.Hangman
import xyz.jmullin.hangman.entity.PuzzleDisplay
import java.util.concurrent.TimeUnit

/**
 * The meat of the game. Runs a puzzle against a set of bot implementations, generates
 * results for each player including scores.
 */
class Puzzle(internal val solution: String,
             private val dictionary: List<String>,
             players: List<HangmanBot>) {
    companion object {
        val MaxStrikes = 8
        val MaxPuzzleScore = 10
    }

    private val initialState = solution.map { "_" }.joinToString("")

    private val validCharacters = 'a'..'z'

    data class PlayerState(val implementation: HangmanBot,
                           val puzzle: String,
                           val strikes: Int = 0,
                           val previousGuesses: List<Char> = emptyList(),
                           val misses: List<Char> = emptyList()) {
        fun done(solution: String) = puzzle == solution || strikes == MaxStrikes
    }

    data class PlayerResult(val name: String, val score: Int)

    var playerStatuses = players.map { PlayerState(it, initialState) }
        private set(newStatuses) {
            field = newStatuses
        }

    suspend fun start(): List<PlayerResult> {
        println("Puzzle '$solution':")

        val legalWords = dictionary.filter { it.length == solution.length }
        playerStatuses.forEach {
            it.implementation.initBot(solution.length, legalWords)
        }

        var turnStart: Long
        while(playerStatuses.any { !it.done(solution) }) {
            turnStart = System.currentTimeMillis()
            val jobs = playerStatuses.map { player ->
                async {
                    if(player.done(solution)) {
                        '?'
                    } else {
                        getNextGuess(player)
                    }
                }
            }

            val guesses = jobs.map { withTimeoutOrNull(Hangman.MaxTurnTime, TimeUnit.MILLISECONDS) { it.await() } ?: '!' }
            if(Hangman.visualize) delay(maxOf(turnStart - System.currentTimeMillis() + PuzzleDisplay.TurnDelay, 0))
            playerStatuses = playerStatuses.zip(guesses).map { (player, guess) -> doTurn(player, guess) }
        }

        val results = playerStatuses.map {
            PlayerResult(it.implementation.name, if(it.strikes == MaxStrikes) 0 else MaxPuzzleScore - it.strikes)
        }.sortedBy { -it.score }

        println(results.joinToString("\n") { "\t${it.name}: ${it.score}" })

        return results
    }

    private fun doTurn(player: PlayerState, guess: Char): PlayerState {
        if(player.done(solution)) return player

        if(guess == '!') {
            return player.copy(strikes = MaxStrikes)
        }

        val newPuzzle = player.puzzle.mapIndexed { index, char ->
            if(solution[index] == guess) guess else char
        }.joinToString("")
        val miss = newPuzzle == player.puzzle

        return player.copy(
            puzzle = newPuzzle,
            strikes = if(miss) player.strikes + 1 else player.strikes,
            previousGuesses = player.previousGuesses + guess,
            misses = if(miss) player.misses + guess else player.misses
        )
    }

    private suspend fun getNextGuess(player: PlayerState): Char {
        return try {
            val guess = player.implementation.nextGuess(player.puzzle, player.strikes, player.previousGuesses, player.misses)
            if(!validCharacters.contains(guess)) '?' else guess
        } catch (e: Exception) {
            '!'
        }
    }
}
package xyz.jmullin.hangman.game

import kotlinx.coroutines.experimental.delay
import xyz.jmullin.drifter.extensions.shuffle
import xyz.jmullin.hangman.Hangman
import xyz.jmullin.hangman.Visualizer
import xyz.jmullin.hangman.entity.PuzzleDisplay

/**
 * Tournament definition. Creates a set of puzzles and runs them on start().
 *
 * @param numPuzzles The number of puzzles to run in the tournament.
 * @param players List of bot implementations to run against the puzzles list.
 */
class Tournament(numPuzzles: Int, players: List<HangmanBot>) {
    private val words: List<String> = javaClass.getResource("/data/words.txt").readText().lines()
    private val solution = words.shuffle().take(numPuzzles)
    private val rounds = solution.map { Puzzle(it, words, players) }

    suspend fun start(): Map<String, Int> {
        val scores = mutableMapOf<String, Int>()

        rounds.mapIndexed { i, puzzle ->
            if(Hangman.visualize) {
                Visualizer.puzzleDisplay.round = i
                Visualizer.puzzleDisplay.puzzle = puzzle
            }
            val result = puzzle.start()
            result.apply {
                forEach { scores[it.name] = scores.getOrDefault(it.name, 0) + it.score }
                if(Hangman.visualize) {
                    Visualizer.puzzleDisplay.applyScores(scores)
                    delay((PuzzleDisplay.RefreshDelay * Hangman.delayMultiplier).toLong())
                }
            }
        }
        if(Hangman.visualize) {
            Visualizer.puzzleDisplay.round = Hangman.NumPuzzles
        }

        return scores
    }
}
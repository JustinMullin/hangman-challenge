package xyz.jmullin.hangman.bot

import xyz.jmullin.hangman.game.HangmanBot

/**
 * Sajak's flawless winning strategy, as seen on TV.
 */
class PatSajakBot : HangmanBot {
    override val name = "patSajakBot"

    override fun initBot(wordLength: Int, dictionary: List<String>) {}

    override fun nextGuess(puzzle: String, strikes: Int, previousGuesses: List<Char>, misses: List<Char>): Char {
        return "rstlne".getOrNull(previousGuesses.size) ?: '?'
    }
}
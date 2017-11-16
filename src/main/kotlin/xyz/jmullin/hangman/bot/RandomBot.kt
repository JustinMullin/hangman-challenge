package xyz.jmullin.hangman.bot

import xyz.jmullin.drifter.extensions.rElement
import xyz.jmullin.hangman.game.HangmanBot

class RandomBot : HangmanBot {
    override val name = "randomBot"

    override fun initBot(wordLength: Int, dictionary: List<String>) {}

    override fun nextGuess(puzzle: String, strikes: Int, previousGuesses: List<Char>, misses: List<Char>): Char {
        return rElement(('a' until 'z').toList().filter { !previousGuesses.contains(it) })
    }
}
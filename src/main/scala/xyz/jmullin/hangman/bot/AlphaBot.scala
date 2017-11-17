package xyz.jmullin.hangman.bot

import xyz.jmullin.hangman.game.BaseScalaBot

/**
 * Now we know our ABCs.
 */
class AlphaBot extends BaseScalaBot {
  override def getName = "alphaBot"

  override def initBot(wordLength: Int,
                       dictionary: List[String]): Unit = {}

  override def nextGuess(puzzle: String,
                         strikes: Int,
                         previousGuesses: List[Char],
                         misses: List[Char]): Char = {
    ('a' until 'z')(previousGuesses.size)
  }
}

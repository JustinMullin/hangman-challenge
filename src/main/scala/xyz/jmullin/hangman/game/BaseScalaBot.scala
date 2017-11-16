package xyz.jmullin.hangman.game

import scala.collection.JavaConverters._

/**
 * Convenience class which provides a bot interface utilizing Scala standard types instead of Java ones,
 * to simplify conversions of java.util.List and java.lang.Character.
 */
trait BaseScalaBot extends HangmanBot {
  override def initBot(wordLength: Int,
                       dictionary: java.util.List[String]): Unit = {
    initBot(wordLength, dictionary.asScala.toList)
  }
  override def nextGuess(puzzle: String,
                         strikes: Int,
                         previousGuesses: java.util.List[java.lang.Character],
                         misses: java.util.List[java.lang.Character]): Char = {
    nextGuess(puzzle, strikes, previousGuesses.asScala.map(_.toChar).toList, misses.asScala.map(_.toChar).toList)
  }

  def initBot(wordLength: Int, dictionary: List[String]): Unit
  def nextGuess(puzzle: String, strikes: Int, previousGuesses: List[Char], misses: List[Char]): Char
}

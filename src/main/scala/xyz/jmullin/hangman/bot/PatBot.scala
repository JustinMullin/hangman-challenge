package xyz.jmullin.hangman.bot

import xyz.jmullin.hangman.game.BaseScalaBot

/**
  * Guess a good first letter, filter by frequency from there.
  *
  * This bot lives dangerously.
  */
class PatBot extends BaseScalaBot {
  import PatBot._

  private var currentWordLength: Int = 0
  private var currentDictionary: List[String] = Nil
  private var seeds: Array[Char] = Array()

  override def getName = "patBot"

  override def initBot(wordLength: Int,
                       dictionary: List[String]): Unit = {
    currentDictionary = dictionary
    currentWordLength = wordLength
    seeds = InitialGuesses(wordLength)
  }

  override def nextGuess(puzzle: String,
                         strikes: Int,
                         previousGuesses: List[Char],
                         misses: List[Char]): Char = {
    if (previousGuesses.isEmpty || (previousGuesses.size == misses.size)) {
      // Initial Guess -- choose the proper index.
      seeds.charAt(misses.size)
    } else {
      println(s"Currently have ${previousGuesses.size} guesses and ${misses.size} misses.")
      // Secondary Guess -- Look up the current puzzle, guess from that.
      // We're finding the indexes of every guessed letter, which will allow us to find words that contain exact matches.
      val matched = puzzle
        .toList
        .zipWithIndex
        .filter { case (c, _) => c != '_' }

      // Select all words from the dictionary that match the current letter positions.
      val subDict: List[String] = currentDictionary.filter { w =>
        matched.forall {
          case (c, idx) => w.charAt(idx) == c
        }
      }

      currentDictionary = subDict

      // Attempt to select the next letter -- the most frequently occurring unused letter.
      subDict.iterator
        .flatMap(_.toList)
        .filterNot(c => previousGuesses.contains(c))
        .filterNot(c => misses.contains(c))
        .toList
        .groupBy(identity)
        .mapValues(_.size)
        .maxBy(_._2)
        ._1
    }
  }
}

object PatBot {
  private val InitialGuesses: Map[Int, Array[Char]] = Map(
    1 -> Array('a', 'i'),
    2 -> Array('a', 'o', 'e', 'i', 'u', 'm', 'b', 'h'),
    3 -> Array('a', 'e', 'o', 'i', 'u', 'y', 'h', 'b', 'c', 'k'),
    4 -> Array('a', 'e', 'o', 'i', 'u', 'y', 's', 'b', 'f'),
    5 -> Array('s', 'e', 'a', 'o', 'i', 'u', 'y', 'h'),
    6 -> Array('e', 'a', 'i', 'o', 'u', 's', 'y'),
    7 -> Array('e', 'i', 'a', 'o', 'u', 's'),
    8 -> Array('e', 'i', 'a', 'o', 'u'),
    9 -> Array('e', 'i', 'a', 'o', 'u'),
    10 -> Array('e', 'i', 'o', 'a', 'u'),
    11 -> Array('e', 'i', 'o', 'a', 'd'),
    12 -> Array('e', 'i', 'o', 'a', 'f'),
    13 -> Array('i', 'e', 'o', 'a'),
    14 -> Array('i', 'e', 'o'),
    15 -> Array('i', 'e', 'a'),
    16 -> Array('i', 'e', 'h'),
    17 -> Array('i', 'e', 'r'),
    18 -> Array('i', 'e', 'a'),
    19 -> Array('i', 'e', 'a'),
    20 -> Array('i', 'e')
  )

  private val Letters: Array[Char] = Array('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z')
}

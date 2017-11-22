(ns xyz.jmullin.hangman.bot.JasonBot
  "This bot uses a regular expression that excludes previous guesses to narrow
  the dictonary, then uses letter popularity to determine the next letter to
  guess.

  Limiting the list of possible words speeds up the letter popularity
  calculation and, more importantly, provides a more accurate letter popularity
  as more words are excluded from that calculation.

  Example: If the word is 'setter' and the first guess is 'e' then the puzzle
  is '_e__e_'. We know the unknown slots cannot have e's in them so the regular
  expression used to select possible words from the dictonary is '[^e]e[^e][^e]e[^e]'.
  Thus words like 'seeler' will not be considered and the popularity of the
  letter 'l' will not be inflated."
  (:gen-class
   :implements [xyz.jmullin.hangman.game.HangmanBot])
  (:require [clojure.string :as str]))

;;; The current dictionary of possible words. This atom is updated during each
;;; guess following the first successful guess.
(def cur-dictionary (atom []))

;;; Only used as long as the puzzle contains all blanks. After the first
;;; successful guess the letter popularity is re-calculated for every
;;; subsequent guess.
(def initial-letterpopularity (atom []))

(defn wordlist->letterpopularity
  "Converts a seq of words into a vector of letters sorted from the most
  frequently used letter (aka the most popular) in the list of words to the
  least frequently used."
  [wordlist]
  (->> wordlist
       (map seq)
       flatten
       frequencies
       (sort-by val)
       reverse
       (map first)
       vec))

(defn puzzle->pattern
  "Converts a puzzle (see `-nextGuess` method) to a regular expression pattern
  that is used to narrow the dictionary of possible words."
  [puzzle exclude-letters]
  (re-pattern (str/replace puzzle #"_" (str "[^" (str/join exclude-letters) "]"))))

(defn pop-initial-letterpopularity
  "Return the first letter from `initial-letterpopularity` and removes the first letter
  from that list."
  []
  (let [first-letter (first @initial-letterpopularity)]
    (reset! initial-letterpopularity (drop 1 @initial-letterpopularity))
    first-letter))

(defn not-in?
  "Returns true if `item` is not in `coll`."
  [coll item]
  (> 0 (.indexOf coll item)))

(defn puzzle-letters
  "Given a `puzzle` (see `-nextGuess` method) this function returns a seq of
  just the letters, with the blanks (i.e. underscores, '_') removed."
  [puzzle]
  (seq (str/replace puzzle #"_" "")))

(defn remove-previous-guesses
  "Removes from `letters` any item in `previous-guesses`."
  [previous-guesses letters]
  (filter #(not-in? previous-guesses %) letters))

(defn words-matching
  "Returns all items from `dictionary` that match the regular expression `regex`."
  [regex dictionary]
  (filter #(re-find regex %) dictionary))

(defn recalc-and-guess
  "Recalculates the letter popularity list and possible words dictionary for
  the given puzzle state. Returns the most popular letter."
  [puzzle previous-guesses]
  (let [puzzle-pattern (puzzle->pattern puzzle previous-guesses)]
    (->> @cur-dictionary
         (words-matching puzzle-pattern)
         (reset! cur-dictionary)
         wordlist->letterpopularity
         (remove-previous-guesses previous-guesses)
         first)))

(defn -getName
  "Your Nerdery LDAP username (e.g. jmullin), for purposes of tracking and identification."
  [this]
  "jmizher")

(defn -initBot
  "Called by the game at the start of each round to provide information about the next puzzle and
   allow the player to perform prep activities. There is no time limit for prep activities, but
   please keep it reasonable!

   wordLength: The number of characters in the next puzzle.
   dictionary: A list of all potential words from the official dictionary, prefiltered to the word
               length for your convenience."
  [this wordLength dictionary]
  (reset! cur-dictionary dictionary)
  (reset! initial-letterpopularity (wordlist->letterpopularity dictionary)))

(defn -nextGuess
  "Called by the game each turn, presenting the current puzzle state. The player should return
   their next letter guess. Guessing a letter that is not in the puzzle or guessing a letter that
   has already been guessed will be considered a strike.

   The player will fail the current puzzle if this method takes more than 1 second to complete.

   puzzle: The current puzzle state, with blanks represented by underscores, e.g. 'gat_k__p_r'
   strikes: The number of incorrect guesses the player has made.
   previousGuesses: List of previous guesses (both right and wrong guesses) from this player.
   misses: List of incorrect letters previously guessed by the player."
  [this puzzle strikes previousGuesses misses]
  (if (empty? (puzzle-letters puzzle))
    (pop-initial-letterpopularity)
    (recalc-and-guess puzzle previousGuesses)))

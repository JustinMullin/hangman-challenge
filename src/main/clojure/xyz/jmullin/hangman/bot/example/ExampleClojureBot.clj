(ns xyz.jmullin.hangman.bot.example.ExampleClojureBot
  (:gen-class
   :implements xyz.jmullin.hangman.game.HangmanBot))

(defn -getName
  "Your Nerdery LDAP username (e.g. jmullin), for purposes of tracking and identification."
  [this]
  "clojureBot")

(defn -initBot
  "Called by the game at the start of each round to provide information about the next puzzle and
   allow the player to perform prep activities. There is no time limit for prep activities, but
   please keep it reasonable!

   wordLength: The number of characters in the next puzzle.
   dictionary: A list of all potential words from the official dictionary, prefiltered to the word
               length for your convenience."
  [this wordLength dictionary]
  nil)

(defn -nextGuess
  "Called by the game each turn, presenting the current puzzle state. The player should return
   their next letter guess. Guessing a letter that is not in the puzzle or guessing a letter that
   has already been guessed will be considered a strike.

   The player will fail the current puzzle if this method takes more than 1 second to complete.

   puzzle: The current puzzle state, with blanks represented by underscores, e.g. 'gat_k__p_r'
   strikes: The number of incorrect guesses the player has made.
   previousGuesses: List of previous guesses from this player.
   misses: List of incorrect letters previously guessed by the player."
  [this puzzle strikes previousGuesses misses]
  \?)

package xyz.jmullin.hangman.game

/**
 * Base bot interface. Implement this to play the game!
 */
interface HangmanBot {
    /**
     * Your Nerdery LDAP username (e.g. jmullin), for purposes of tracking and identification.
     */
    val name: String

    /**
     * Called by the game at the start of each round to provide information about the next puzzle and
     * allow the player to perform prep activities. There is no time limit for prep activities, but
     * please keep it reasonable!
     *
     * @param wordLength The number of characters in the next puzzle.
     * @param dictionary A list of all potential words from the official dictionary, prefiltered to the
     *                   word length for your convenience.
     */
    fun initBot(wordLength: Int, dictionary: List<String>)

    /**
     * Called by the game each turn, presenting the current puzzle state. The player should return
     * their next letter guess. Guessing a letter that is not in the puzzle or guessing a letter that
     * has already been guessed will be considered a strike.
     *
     * The player will fail the current puzzle if this method takes more than 1 second to complete.
     *
     * @param puzzle The current puzzle state, with blanks represented by underscores, e.g. "gat_k__p_r"
     * @param strikes The number of incorrect guesses the player has made.
     * @param previousGuesses List of previous guesses from this player.
     * @param misses List of incorrect letters previously guessed by the player.
     *
     * @return The player's next guess.
     */
    fun nextGuess(puzzle: String, strikes: Int, previousGuesses: List<Char>, misses: List<Char>): Char
}
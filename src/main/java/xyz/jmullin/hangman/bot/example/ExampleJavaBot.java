package xyz.jmullin.hangman.bot.example;

import java.util.List;
import org.jetbrains.annotations.NotNull;
import xyz.jmullin.hangman.game.HangmanBot;

/**
 * Copy this class into the `xyz.jmullin.hangman.bot` package to start implementing your bot!
 */
public class ExampleJavaBot implements HangmanBot {
    /**
     * Your Nerdery LDAP username (e.g. jmullin), for purposes of tracking and identification.
     */
    @NotNull
    @Override
    public String getName() {
        return "javaBot";
    }

    /**
     * Called by the game at the start of each round to provide information about the next puzzle and
     * allow the player to perform prep activities. There is no time limit for prep activities, but
     * please keep it reasonable!
     *
     * @param wordLength The number of characters in the next puzzle.
     * @param dictionary A list of all potential words from the official dictionary, prefiltered to the
     *                   word length for your convenience.
     */
    @Override
    public void initBot(int wordLength,
                        @NotNull List<String> dictionary) {

    }

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
    @Override
    public char nextGuess(@NotNull String puzzle,
                          int strikes,
                          @NotNull List<Character> previousGuesses,
                          @NotNull List<Character> misses) {
        return '?';
    }
}

package xyz.jmullin.hangman.bot;

import java.util.List;
import java.util.Random;
import org.jetbrains.annotations.NotNull;
import xyz.jmullin.hangman.game.HangmanBot;

/**
 * Makes poor choices, randomly.
 */
public class RandomBot implements HangmanBot {
    private final Random random = new Random();

    @NotNull
    public String getName() {
        return "randomBot";
    }

    public void initBot(int wordLength,
                        @NotNull List<String> dictionary) {}

    public char nextGuess(@NotNull String puzzle,
                          int strikes,
                          @NotNull List<Character> previousGuesses,
                          @NotNull List<Character> misses) {
        return (char)('a' + random.nextInt('z'-'a'));
    }
}

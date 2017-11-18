# Nerdery JVM Hangman Challenge

In this challenge you will write a bot which plays [Hangman][1], competing against other player's bots.
Bots will receive points for each successfully guessed word, with more points being awarded to bots with
fewer incorrect guesses.

## The Rules:

1. A tournament consists of 50 puzzles.
1. For each puzzle in a tournament:
    1. A random word is chosen as the solution. Words will not be duplicated verbatim within a tournament.
    See `words.txt` for the full list of possible words.
    1. While any bots remain which have not solved or failed the puzzle:
        1. Each bot is presented with their current puzzle state, and allowed to guess one letter.
            * If the letter is not in the puzzle or was already guessed by this bot, the bot is given a strike.
            * Otherwise all instances of that letter in the puzzle will be revealed to the bot for their next turn.
        1. Any bots which have 8 strikes have failed the puzzle and are eliminated from that puzzle.
        1. Any bots which have correctly guessed all letters in the puzzle have solved the puzzle.
        1. Any bot which takes more than one second to guess a letter during their turn is considered to
        have failed the puzzle.
    1. After all bots have either solved or failed the puzzle, each bot gains points using the formula:
    `charactersRevealed + (50 - strikes*5)`.
1. Points are summed for each bot across the 50 puzzles. The bot with the most points wins the tournament.
1. In the event of a tie, a victor will be chosen based on arbitrary and capricious judging of the tied
bot's implementations.

## Implementing Your Bot

Example bots are provided in Kotlin, Java and Scala. To implement a bot, open the source directory for your
chosen language and copy one of the example bots from the `xyz.jmullin.hangman.bot.example` package into
`xyz.jmullin.hangman.bot`. Feel free to submit a PR adding support for your favorite JVM language if it's not already
represented.

Bots implement the `xyz.jmullin.hangman.game.HangmanBot` interface. More specific documentation pertaining
to implementation can be found there. Any bots implementing the `HangmanBot` interface which are located
in the `xyz.jmullin.hangman.bot` package will be automatically included in the tournament.

When you're happy with your bot, submit a PR including your bot implementation. We'll run the final tournament
and determine a victor.

## Testing Your Bot

You can run a test tournament in the visualizer or headless mode. In the visualizer you can see all the bot
implementations competing against each other in realtime, and control simulation speed. Headless mode
eschews display and animations, and outputs the results of each puzzle and the final scores to standard out;
it's useful for rapid testing and comparison of new strategies.

`./gradlew run`: Run a tournament in the visualizer.

`./gradlew runHeadless`: Run a text-based tournament.

Feel free to stack up multiple versions of testing bots locally to see how they perform against each other,
but please only include a single bot named with your Nerdery LDAP username as your final submission in the PR.

## Questions or Concerns?

Direct 'em to Justin Mullin, via Nerdery chat or at jmullin@nerdery.com.

 [1]: https://en.wikipedia.org/wiki/Hangman_\(game\)
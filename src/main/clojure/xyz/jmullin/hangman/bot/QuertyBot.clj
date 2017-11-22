(ns xyz.jmullin.hangman.bot.QuertyBot
  (:gen-class
   :implements [xyz.jmullin.hangman.game.HangmanBot]))

(defn -getName [this] "quertyBot")
(defn -initBot [this wordLength dictionary] nil)
(defn -nextGuess [this puzzle strikes previousGuesses misses]
  (get "qwertyuiopasdfghjklzxcvbnm" (count previousGuesses)))
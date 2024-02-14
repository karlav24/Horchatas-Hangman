package com.example.horchatas_hangman

class Hangman (private val words: List<String>){
    // initialize selectedWord to emptyString, this word will be selected at random when a new game starts
    var selectedWord = ""

    // initialize guesses to MutableSet to avoid duplicate entries and to provide efficient checks later
    private var correctlyGuessed = mutableSetOf<Char>()
    private var incorrectlyGuessed = mutableSetOf<Char>()

    // initialize max attempts before game is over
    private var maxAttempts = 6

    // variable for how many attempts left after choosing the wrong character
    var attemptsLeft = maxAttempts
        private set // ensure that attemptsLeft can only be modified within class

    // concatenate the guessed letters into a string
    val guessedLetters: String
        get() = (correctlyGuessed + incorrectlyGuessed).joinToString(" ")

    // produce the currently displayed word with letters that are yet to be guessed
    val currentWordDisplay: String
        get() = selectedWord.map { if (it in correctlyGuessed) it else '_' }.joinToString(" ")

    // check to see if the game is lost or won
    val lostGame: Boolean
        get() = attemptsLeft <= 0

    val wonGame: Boolean
        get() = selectedWord.all { it in correctlyGuessed }

    // initializer for starting a new game when new instance is created
    init {
        newGame()
    }

    // reset the game to a new game state
    fun newGame() {
        selectedWord = words.random().uppercase() // select a word at random to guess
        correctlyGuessed.clear() // clear sets
        incorrectlyGuessed.clear()
        attemptsLeft = maxAttempts
    }

    // process a guessed letter and update game state as the game is played
    fun guess(letter: Char): Boolean {
        val guessedLetter = letter.uppercaseChar() // normalize the letter

        // condition for when the letter has already been guessed
        if (guessedLetter in correctlyGuessed || guessedLetter in incorrectlyGuessed) {
            return false
        }

        // if guessed word is in the selected word then add to set of correct guesses
        if (selectedWord.contains(guessedLetter)) {
            correctlyGuessed.add(guessedLetter)
        } else {
            incorrectlyGuessed.add(guessedLetter)
            attemptsLeft--
        }
        return guessedLetter in correctlyGuessed
    }
}
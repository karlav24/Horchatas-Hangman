package com.example.horchatas_hangman

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    // initialized views in onCreate
    private lateinit var hangmanGame: Hangman
    private lateinit var goalWord: TextView // word that is being guessed
    private lateinit var hangmanImage: ImageView
    private lateinit var newGameButton: Button
    private lateinit var hintButton: Button
    private lateinit var hintTextView: TextView
    private val letters = mutableListOf<Button>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        hangmanGame = Hangman(getWords())
        
        initializeViews()
        initializeButtons()
        newGameButton()
        hintButton()
        updateUI()
    }

    // get the list of words for the hangman game
    private fun getWords(): List<String> {
        return listOf("Android", "Kotlin", "Variable", "Function", "Class")
    }

    // initialize the interface elements from the layout
    private fun initializeViews() {
        goalWord = findViewById(R.id.word)
        hangmanImage = findViewById(R.id.hangman)
        newGameButton = findViewById(R.id.newgame)
        hintButton = findViewById(R.id.hint)
        hintTextView = findViewById(R.id.hinttext)
    }

    // initialize the buttons and add to a list called letters
    private fun initializeButtons() {
        val letterIds = listOf(
            R.id.A, R.id.B, R.id.C, R.id.D, R.id.E, R.id.F, R.id.G, R.id.H, R.id.I,
            R.id.J, R.id.K, R.id.L, R.id.M, R.id.N, R.id.O, R.id.P, R.id.Q, R.id.R,
            R.id.S, R.id.T, R.id.U, R.id.V, R.id.W, R.id.X, R.id.Y, R.id.Z
        )
        letterIds.forEach { id ->
            findViewById<Button>(id).also { button ->
                letters.add(button)
                button.setOnClickListener { checkGuesses(button.text.first()) }
            }
        }
    }

    // set up a new game when the new game button is pressed by the user
    private fun newGameButton() {
        newGameButton.setOnClickListener {
            hangmanGame.newGame()
            updateUI()
        }
    }

    // set up the logic for the hint with a click listener
    private fun hintButton() {
        hintButton.setOnClickListener {
            // Your hint logic here
        }
    }

    // handle letter guess and update the game state UI
    private fun checkGuesses(letter: Char) {
        if (hangmanGame.guess(letter)) {
            goalWord.text = hangmanGame.currentWordDisplay
        } else {
            updateHangman()
        }
        checkGameStatus()
    }

    // UI updated based on the current game state
    private fun updateUI() {
        goalWord.text = hangmanGame.currentWordDisplay
        hangmanImage.setImageResource(getHangman(hangmanGame.attemptsLeft))
        letters.forEach { button ->
            button.isEnabled = !hangmanGame.guessedLetters.contains(button.text.toString())
        }
    }

    // consulted GPT on how to use resource ID for drawable files based on how many attempts the user has
    // also found that we can use getIdentifier which is part of the Android Resources class to obtain the 
    // integer unique  ID associated with a resource name at runtime
    private fun getHangman(attemptsLeft: Int): Int {
        val resourceName = when (attemptsLeft) {
            5 -> "head"
            4 -> "torso"
            3 -> "arm1"
            2 -> "arm2"
            1 -> "leg1"
            0 -> "leg2"
            else -> "init"
        }
        return resources.getIdentifier(resourceName, "drawable", packageName)
    }

    // update the hangman image to the current state of the game
    private fun updateHangman() {
        hangmanImage.setImageResource(getHangman(hangmanGame.attemptsLeft))
    }

    // check if the user has won or lost the game
    private fun checkGameStatus() {
        if (hangmanGame.wonGame) {
            hintTextView.text = getString(R.string.win_message)
            disableButtons()
        } else if (hangmanGame.lostGame) {
            hintTextView.text = getString(R.string.lose_message)
            hangmanImage.setImageResource(R.drawable.dead)
            disableButtons()
        }
    }

    // method ot disable letter buttons when the game is over
    private fun disableButtons() {
        letters.forEach { it.isEnabled = false }
    }
}
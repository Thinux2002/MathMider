package com.cscorner.mathminder

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat

class MainActivity2 : AppCompatActivity() {

    private lateinit var button : Button
    private lateinit var button2 : Button

    private lateinit var questionTextView: TextView
    private lateinit var answerEditText: EditText
    private lateinit var submitButton: Button

    private val questions = arrayOf("2 + 2 =", "5 - 3 =", "6 * 4 =", "10 / 2 =","1+1=","10*5=","5+2","12*2","5*7","40/4","60/12","40/8","72/8","81/9","8*6","10+9","8*3","4*9")
    private val answers = arrayOf("4", "2", "24", "5","2","50","7","24","35","10","5","5","9","9","48","19","24","36")

    private var currentQuestionIndex = 0
    private var score = 0
    private var highestScore = 0


    private lateinit var timer: CountDownTimer
    private var timeLeftInMillis: Long = 40000

    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        highestScore = sharedPreferences.getInt("highest_score", 0)


        // Initialize buttons
        button = findViewById(R.id.button)
        button2 = findViewById(R.id.button2)

        // Set click listener for button
        button.setOnClickListener {
            // Navigate to MainActivity2
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }

        // Set click listener for button2
        button2.setOnClickListener {
            // Navigate to MainActivity3
            val intent = Intent(this, MainActivity3::class.java)
            startActivity(intent)
        }

        questionTextView = findViewById(R.id.textView6)
        answerEditText = findViewById(R.id.editTextNumber)
        submitButton = findViewById(R.id.button3)

        showNextQuestion()

        submitButton.setOnClickListener {
            checkAnswer()
        }

        startTimer()
    }

    private fun showNextQuestion() {
        questionTextView.text = questions[currentQuestionIndex]
        answerEditText.text.clear()
    }

    private fun checkAnswer() {
        val userAnswer = answerEditText.text.toString().trim()
        if (userAnswer == answers[currentQuestionIndex]) {
            score++
            // Update score display
            findViewById<TextView>(R.id.textView4).text = "Current score is: $score"
        }
        // Move to the next question
        currentQuestionIndex++
        if (currentQuestionIndex < questions.size) {
            showNextQuestion()
        } else {
            gameOver()
        }
    }


    private fun startTimer() {
        timer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                val secondsLeft = millisUntilFinished / 1000
                findViewById<TextView>(R.id.textView5).text = "$secondsLeft seconds left"
            }

            override fun onFinish() {
                gameOver()
            }
        }.start()
    }

    private fun gameOver() {
        timer.cancel()
        if (score > highestScore) {
            highestScore = score
            val editor = sharedPreferences.edit()
            editor.putInt("highest_score", highestScore)
            editor.apply()
        }

        val alertDialog = AlertDialog.Builder(this).apply {
            setTitle("Game Over")
            setMessage("Your final score is: $score\nHighest score: $highestScore")
            setPositiveButton("Start Again") { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
                val intent = Intent(this@MainActivity2, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            setNeutralButton("Stop") { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
                val intent = Intent(this@MainActivity2, MainActivity2::class.java)
                startActivity(intent)
                finish()
            }
            setCancelable(false)
        }.create()

        alertDialog.show()

        // Customizing dialog text appearance
        val messageTextView = alertDialog.findViewById<TextView>(android.R.id.message)
        val positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
        val neutralButton = alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL)

        messageTextView?.apply {
            setTextColor(ContextCompat.getColor(this@MainActivity2, R.color.black))
            textSize = 18f
            typeface = Typeface.DEFAULT_BOLD
        }

        positiveButton?.setTextColor(ContextCompat.getColor(this@MainActivity2, R.color.green))
        neutralButton?.setTextColor(ContextCompat.getColor(this@MainActivity2, R.color.red))
    }

}

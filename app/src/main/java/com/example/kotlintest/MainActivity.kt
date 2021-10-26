package com.example.kotlintest

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.widget.Toast
import com.example.kotlintest.databinding.ActivityMainBinding
import com.example.kotlintest.models.Question

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var numTextViews: List<TextView>
    private lateinit var questionTextView: TextView

    private val questions: MutableList<Question> = Question.getBaseData().toMutableList()

    private var currentQuestionIndex: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater) // инициализируем kotlin bindings
        setContentView(binding.root)

        questionTextView = binding.content.textQuestion
        numTextViews = listOf(
            binding.content.textNum1,
            binding.content.textNum2,
            binding.content.textNum3,
            binding.content.textNum4,
            binding.content.textNum5,
            binding.content.textNum6
        )

        updateUi()

        configureHelpButtons()
        configureArrowButtons()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data == null) return
        if (data.getBooleanExtra(HelpActivity.extraOutHelpWasUsed, false))
            questions[currentQuestionIndex].isHelpUsed = true
    }

    private fun configureArrowButtons() {
        binding.content.buttonLeft.setOnClickListener {
            if (currentQuestionIndex > 0) {
                currentQuestionIndex--
                updateUi()
            }
        }
        binding.content.buttonRight.setOnClickListener {
            if (currentQuestionIndex < questions.size - 1) {
                currentQuestionIndex++
                updateUi()
            }
        }
    }

    private fun configureHelpButtons() {
        // вместо val button = findViewById<Button>(R.id.button_help)
        binding.content.buttonHelp // получаем элемент View через kotlin bindings
            .setOnClickListener {
                startActivityForResult(
                    Intent(this, HelpActivity::class.java).apply {
                        putExtra(HelpActivity.extraQuestionIdxKey, currentQuestionIndex)
                    }, 0
                )
            }
    };

    private fun updateUi() {
        val question = questions[currentQuestionIndex];
        questionTextView.setText(question.textId)

        val nums = (question.other + listOf(question.rightAnswer)).shuffled()
        nums.forEachIndexed { idx, it ->
            val isRightAnswer = it == question.rightAnswer

            numTextViews[idx].text = it.toString()
            numTextViews[idx].setOnClickListener {
                getString(
                    when {
                        question.isAnswerGiven -> R.string.answer_given
                        isRightAnswer -> (if (question.isHelpUsed) R.string.answer_right_help_used else R.string.answer_right)
                        else -> R.string.answer_wrong
                    }
                ).let {
                    Toast.makeText(
                        applicationContext,
                        it,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                if (isRightAnswer) questions[currentQuestionIndex].isAnswerGiven = true
            }
        }

        binding.content.buttonLeft.visibility =
            if (currentQuestionIndex < 1) View.GONE else View.VISIBLE
        binding.content.buttonRight.visibility =
            if (currentQuestionIndex >= questions.size - 1) View.GONE else View.VISIBLE
    }

    private val currentQuestionIndexKey: String = "currentQuestionIndexKey"
    private val questionsIsAnsweredKey: String = "currentQuestionAnswersKey"
    private val questionsIsHelpUsedKey: String = "questionsIsHelpUsedKey"

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState.apply {
            putInt(currentQuestionIndexKey, currentQuestionIndex)
            putIntegerArrayList(questionsIsAnsweredKey, ArrayList(questions.map { if (it.isAnswerGiven) 1 else 0 }))
            putIntegerArrayList(questionsIsHelpUsedKey, ArrayList(questions.map { if (it.isHelpUsed) 1 else 0 }))
        })
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        savedInstanceState.run {
            currentQuestionIndex = getInt(currentQuestionIndexKey)
            val answerGiven = getIntegerArrayList(questionsIsAnsweredKey)
            val helpUsed = getIntegerArrayList(questionsIsHelpUsedKey)

            questions.forEachIndexed {
                idx, it ->
                it.isAnswerGiven = answerGiven?.get(idx) == 1
                it.isHelpUsed = helpUsed?.get(idx) == 1
            }
        }

        updateUi()
    }
}
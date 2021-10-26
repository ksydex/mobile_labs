package com.example.kotlintest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kotlintest.databinding.ActivityHelpBinding
import com.example.kotlintest.models.Question

class HelpActivity : AppCompatActivity() {
    private var questionIdx: Int = -1
    private lateinit var binding: ActivityHelpBinding

    companion object {
        const val extraQuestionIdxKey = "com.kotlintest.extraQuestionIdxKey"
        const val extraOutHelpWasUsed = "com.kotlinTest.extraOutHelpWasUsed"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        questionIdx = intent.getIntExtra(extraQuestionIdxKey, -1)
        binding.textView2.setText(Question.getBaseData()[questionIdx].helpTextId)
        setHelpUsedResult()
    }

    private fun setHelpUsedResult() {
        setResult(RESULT_OK, Intent().apply {
            putExtra(extraOutHelpWasUsed, true)
        })
    }
}
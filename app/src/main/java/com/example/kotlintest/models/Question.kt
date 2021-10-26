package com.example.kotlintest.models

import com.example.kotlintest.R
import java.lang.Exception

class Question(val textId: Int, val rightAnswer : Int, val other: List<Int>, val helpTextId : Int) {
    var isHelpUsed: Boolean = false
    var isAnswerGiven: Boolean = false

    init {
        if(other.size != 5) throw Exception("Size of .other should be 5")
    }

    companion object {
        fun getBaseData(): List<Question> {
            return listOf(
                Question(R.string.q_simple, 2, listOf(4, 6, 8, 9, 12), R.string.q_simple_help),
                Question(R.string.q_negative, -3, listOf(3, 4, 6, 7, 8), R.string.q_negative_help),
                Question(R.string.q_even, 2, listOf(3, 5, 7, 9, 11), R.string.q_even_help),
                Question(R.string.q_max, 10, listOf(1, 2, 3, 4, 5), R.string.q_max_help)
            )
        }
    }
}
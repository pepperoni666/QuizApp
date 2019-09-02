package com.dummy.quiz_app

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.dummy.quiz_app.data.Question
import com.dummy.quiz_app.data.Quiz

data class QuizState(
    val quizes: Async<List<Quiz>> = Uninitialized
) : MvRxState {
    fun quiz(quizId: Long?): Quiz? = quizes()?.firstOrNull { it.id == quizId }

    fun changeProgress(quizId: Long, newProgress: Int) {
        quizes()?.firstOrNull { it.id == quizId }?.let { it.progress = newProgress }
    }

    fun changeSelectedAnswer(quizId: Long, progress: Int, newSelected: Int) {
        quizes()?.firstOrNull { it.id == quizId }?.let {
            it.questions[progress].selected = newSelected
        }
    }

    fun clearProgress(quizId: Long){
        quizes()?.firstOrNull { it.id == quizId }?.let {
            it.progress = 0
            for(i: Question in it.questions)
                i.selected = null
        }
    }
}
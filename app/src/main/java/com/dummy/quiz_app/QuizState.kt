package com.dummy.quiz_app

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.dummy.quiz_app.data.Quiz

data class QuizState (
    val quizes: Async<List<Quiz>> = Uninitialized
): MvRxState{
    fun quiz(quizId: Long?): Quiz? = quizes()?.firstOrNull { it.id == quizId }

    fun changeProgress(quizId: Long, newProgress: Int) { quizes()?.firstOrNull { it.id == quizId }?.let{ it.progress = newProgress }}
}
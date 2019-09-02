package com.dummy.quiz_app

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.dummy.quiz_app.data.Question
import com.dummy.quiz_app.data.Quiz

data class QuizState(
    val quizzes: Async<List<Quiz>> = Uninitialized
) : MvRxState {

    /**
     * Returns quiz object with given id,
     * or null, in case of none was found.
     * @param quizId
     * @return - quiz object with given quizIz
     */
    fun quiz(quizId: Long?): Quiz? = quizzes()?.firstOrNull { it.id == quizId }

    /**
     * Changes current quiz solving state.
     * Called when user wants to move between questions.
     * @param quizId
     * @param newProgress - new quiz solving state (current question)
     */
    fun changeProgress(quizId: Long, newProgress: Int) {
        quizzes()?.firstOrNull { it.id == quizId }?.let { it.progress = newProgress }
    }

    /**
     * Selects answer for current question.
     * @param quizId
     * @param progress - question posision of current quiz
     * @param newSelected - selected answer
     */
    fun changeSelectedAnswer(quizId: Long, progress: Int, newSelected: Int) {
        quizzes()?.firstOrNull { it.id == quizId }?.let {
            it.questions[progress].selected = newSelected
        }
    }

    /**
     * Delete all answers for current quiz,
     * and resets progress.
     * @param quizId
     */
    fun clearProgress(quizId: Long){
        quizzes()?.firstOrNull { it.id == quizId }?.let {
            it.progress = 0
            for(i: Question in it.questions)
                i.selected = null
        }
    }
}
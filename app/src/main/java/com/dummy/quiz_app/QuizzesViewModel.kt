package com.dummy.quiz_app

import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.ViewModelContext
import com.dummy.quiz_app.app.MvRxViewModel
import com.dummy.quiz_app.app.QuizApplication
import com.dummy.quiz_app.data.Quiz
import com.dummy.quiz_app.data.QuizRepository
import kotlinx.coroutines.launch

class QuizzesViewModel(
    state: QuizState,
    private val quizRepository: QuizRepository
) : MvRxViewModel<QuizState>(state) {

    init {
        setState {
            copy(quizzes = Loading())
        }
        quizRepository.launch {
            val list = quizRepository.getQuizzes()
            setState {
                copy(quizzes = Success(list))
            }
        }
    }

    fun loadMore() {
        quizRepository.launch {
            val list = quizRepository.getQuizzes()
            setState {
                val newQuizzes: ArrayList<Quiz> = ArrayList(quizzes()!!)
                newQuizzes.addAll(list)
                copy(quizzes = Success(newQuizzes))
            }
        }
    }


    companion object : MvRxViewModelFactory<QuizzesViewModel, QuizState> {
        override fun create(viewModelContext: ViewModelContext, state: QuizState): QuizzesViewModel? {
            val quizRepository = viewModelContext.app<QuizApplication>().quizRepository
            return QuizzesViewModel(state, quizRepository)
        }
    }
}
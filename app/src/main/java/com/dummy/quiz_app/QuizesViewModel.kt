package com.dummy.quiz_app

import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.ViewModelContext
import com.dummy.quiz_app.app.MvRxViewModel
import com.dummy.quiz_app.app.QuizApplication
import com.dummy.quiz_app.data.QuizRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class QuizesViewModel(
    state: QuizState,
    quizRepository: QuizRepository
) : MvRxViewModel<QuizState>(state) {

    init {
        setState {
            copy(quizes = Loading())
        }
        quizRepository.launch {
            val list = quizRepository.getQuizes()
            setState {
                copy(quizes = Success(list))
            }
        }

    }


    companion object : MvRxViewModelFactory<QuizesViewModel, QuizState> {
        override fun create(viewModelContext: ViewModelContext, state: QuizState): QuizesViewModel? {
            val quizRepository = viewModelContext.app<QuizApplication>().quizRepository
            return QuizesViewModel(state, quizRepository)
        }
    }
}
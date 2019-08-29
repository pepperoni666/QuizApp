package com.dummy.quiz_app.app

import android.app.Application
import com.dummy.quiz_app.data.QuizRepository

class QuizApplication: Application() {
    val quizRepository = QuizRepository()
}
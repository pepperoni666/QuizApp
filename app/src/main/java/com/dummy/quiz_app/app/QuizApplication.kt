package com.dummy.quiz_app.app

import android.app.Application
import android.widget.Toast
import com.dummy.quiz_app.data.QuizRepository

class QuizApplication : Application() {
    init {
        instance = this
    }

    val quizRepository = QuizRepository()

    companion object {
        private var instance: QuizApplication? = null

        fun showToast(msg: String, makeLong: Boolean) {
            instance?.let {
                Toast.makeText(
                    instance,
                    msg,
                    if (makeLong)
                        Toast.LENGTH_LONG
                    else
                        Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
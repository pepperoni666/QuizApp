package com.dummy.quiz_app.app

import android.app.Application
import android.widget.Toast
import com.dummy.quiz_app.data.QuizRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class QuizApplication : Application() {
    init {
        instance = this
    }

    val quizRepository = QuizRepository()

    companion object {
        private var instance: QuizApplication? = null

        /**
         * Allows to show Toast from anywhere in the project.
         * @param msg = message to be shown in toast
         * @param makeLong = flag, if true: toast will be long, elsewise short
         */
        fun showToast(msg: String, makeLong: Boolean) {
            instance?.let {
                GlobalScope.launch(Dispatchers.Main) {
                    Toast.makeText(
                        it,
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
}
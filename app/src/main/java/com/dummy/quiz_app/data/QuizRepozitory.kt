package com.dummy.quiz_app.data

import kotlinx.coroutines.delay

class QuizRepository {
    suspend fun getQuizes(): List<Quiz> {
        delay(5000)
        return listOf(
            Quiz(
                111,
                "Quiz1",
                "This is quiz one."
            ),
            Quiz(
                222,
                "Quiz2",
                "This is quiz two."
            ),
            Quiz(
                222,
                "Quiz2",
                "This is quiz three."
            )
        )
    }
}
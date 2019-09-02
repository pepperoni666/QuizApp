package com.dummy.quiz_app.data

import com.beust.klaxon.Json

data class Rate(
    val from: Int,
    val to: Int,
    val content: String
)

data class Answer(
    val text: String,
    val isCorrect: Boolean
)

data class Question(
    val text: String,
    val answers: List<Answer>,
    var selected: Int?
)

data class Quiz (
    val id: Long,
    val title: String,
    val info: String,
    val imageUrl: String,
    val questions: List<Question>,
    val rates: List<Rate>,
    var progress: Int
)
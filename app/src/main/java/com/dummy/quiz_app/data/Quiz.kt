package com.dummy.quiz_app.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.beust.klaxon.Json

@Entity(tableName = "rate")
data class Rate(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "quiz_id")
    val quizId: Long,
    @ColumnInfo(name = "from")
    val from: Int,
    @ColumnInfo(name = "to")
    val to: Int,
    @ColumnInfo(name = "content")
    val content: String
)

@Entity(tableName = "answer")
data class Answer(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "question_id")
    val questionId: String,
    @ColumnInfo(name = "text")
    val text: String,
    @ColumnInfo(name = "is_correct")
    val isCorrect: Boolean
)

@Entity(tableName = "question")
data class Question(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String,
    @ColumnInfo(name = "quiz_id")
    var quizId: Long,
    @ColumnInfo(name = "text")
    var text: String,
    @Ignore
    var answers: List<Answer>,
    @ColumnInfo(name = "selected")
    var selected: Int?
){
    constructor() : this("", 0, "", ArrayList<Answer>(), 0)
}

@Entity(tableName = "quiz")
data class Quiz (
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Long,
    @ColumnInfo(name = "title")
    var title: String,
    @ColumnInfo(name = "info")
    var info: String,
    @ColumnInfo(name = "image_url")
    var imageUrl: String,
    @Ignore
    var questions: List<Question>,
    @Ignore
    var rates: List<Rate>,
    @ColumnInfo(name = "progress")
    var progress: Int
){
    constructor() : this(0, "", "", "", ArrayList<Question>(), listOf<Rate>(), 0)
}
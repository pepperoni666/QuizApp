package com.dummy.quiz_app.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Quiz::class, Question::class, Answer::class, Rate::class], version = 1)
abstract class QuizDatabase: RoomDatabase() {
    abstract fun quizDao(): QuizDao
}
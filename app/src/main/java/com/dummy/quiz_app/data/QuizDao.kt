package com.dummy.quiz_app.data

import androidx.room.*

@Dao
interface QuizDao {

    @Query("SELECT COUNT(id) FROM quiz")
    fun getQuizzesCount(): Int

//    @Query("SELECT count(*) FROM rate")
//    suspend fun getRatesCount(): Int
//
//    @Query("SELECT count(*) FROM answer")
//    suspend fun getAnswersCount(): Int
//
//    @Query("SELECT count(*) FROM question")
//    suspend fun getQuestionsCount(): Int

    @Query("SELECT * FROM quiz")
    fun getAllQuizzes(): List<Quiz>

    @Query("SELECT * FROM question WHERE quiz_id LIKE :quizId")
    fun getQuizQuestions(quizId: Long): List<Question>

    @Query("SELECT * FROM rate WHERE quiz_id LIKE :quizId")
    fun getQuizRates(quizId: Long): List<Rate>

    @Query("SELECT * FROM answer WHERE question_id LIKE :questionId")
    fun getQuestionAnswers(questionId: String): List<Answer>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertQuizzes(vararg quiz: Quiz)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertQuestions(vararg question: Question)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAnswers(vararg answer: Answer)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRates(vararg rate: Rate)

    @Query("DELETE FROM quiz")
    fun dropQuizzes()

    @Query("DELETE FROM question")
    fun dropQuestions()

    @Query("DELETE FROM rate")
    fun dropRates()

    @Query("DELETE FROM answer")
    fun dropAnswers()
}
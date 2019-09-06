package com.dummy.quiz_app.data

import android.content.Context
import androidx.room.Room
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import com.beust.klaxon.Parser
import com.dummy.quiz_app.app.QuizApplication
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import kotlinx.coroutines.*
import java.io.IOException
import java.lang.Exception
import java.lang.StringBuilder

class QuizRepository(context: Context) : CoroutineScope by CoroutineScope(Dispatchers.IO) {

    private val httpClient = OkHttpClient()

    private val loadBy = 20
    private var loadedCount = 0

    private val db = Room.databaseBuilder(
        context,
        QuizDatabase::class.java, "quiz-database"
    ).build()

    fun refreshLoadedCounter() {
        loadedCount = 0
    }

    suspend fun isDBEmpty(): Boolean = withContext(Dispatchers.IO) {
        if (db.quizDao().getQuizzesCount() == 0)
            return@withContext true
        return@withContext false
    }

    fun dropAllLocal() {
        db.quizDao().dropAnswers()
        db.quizDao().dropQuestions()
        db.quizDao().dropRates()
        db.quizDao().dropQuizzes()
    }

    suspend fun getSavedQuizzes(): List<Quiz> = withContext(Dispatchers.IO) {
        val quizList = db.quizDao().getAllQuizzes()
        for (i in quizList) {
            i.questions = db.quizDao().getQuizQuestions(i.id)
            for (j in i.questions) {
                j.answers = db.quizDao().getQuestionAnswers(j.id)
            }
            i.rates = db.quizDao().getQuizRates(i.id)
        }
        return@withContext quizList
    }

    /**
     * Asynchronously loads quizzes
     * @return - list of downloaded Quiz objects
     */
    suspend fun getQuizzes(): List<Quiz> = withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url("http://quiz.o2.pl/api/v1/quizzes/$loadedCount/$loadBy")
            .build()

        val quizList = ArrayList<Quiz>()

        try {
            //set timeout for API response to 10s
            withTimeout(20000) {
                //getting list of quizzes
                val response = httpClient.newCall(request).execute()
                if (!response.isSuccessful) {
                    val code = response.code()
                    throw IOException("Api response code $code")
                }

                val quizListJsonObject: JsonObject =
                    Parser.default().parse(StringBuilder(response.body().string())) as JsonObject

                val items: List<JsonObject>? = quizListJsonObject["items"] as? List<JsonObject>

                //every quiz request will run asynchronously
                val awaitItems = ArrayList<Deferred<Unit>>()
                items?.let {
                    for (i: JsonObject in it) {

                        awaitItems.add(async {

                            //message flag to skip quiz because of an error response
                            val skipQuiz = "skip quiz"

                            try {
                                val quizId = i["id"] as Long

                                //requesting each quiz
                                val requestQuizItem = Request.Builder()
                                    .url("http://quiz.o2.pl/api/v1/quiz/$quizId/0")
                                    .build()

                                val quizItemresponse = httpClient.newCall(requestQuizItem).execute()
                                //skip quiz if request fail
                                if (!quizItemresponse.isSuccessful) throw IOException(skipQuiz)

                                val quizItemJsonObject: JsonObject =
                                    Parser.default().parse(StringBuilder(quizItemresponse.body().string())) as JsonObject

                                //parsing questions and answers
                                val questionsJson = quizItemJsonObject["questions"] as? List<JsonObject>
                                val questions = ArrayList<Question>()
                                questionsJson?.let {
                                    for (j: JsonObject in questionsJson) {

                                        val questionId = java.util.UUID.randomUUID().toString().replace("-", "")

                                        val answersJson = j["answers"] as? List<JsonObject>
                                        val answers = ArrayList<Answer>()
                                        answersJson?.let {
                                            for (l: JsonObject in answersJson) {
                                                var isCorrect = false
                                                if (l.containsKey("isCorrect")) {
                                                    isCorrect = (l["isCorrect"] as Int) == 1
                                                }

                                                val text = if (l["text"] is String)
                                                    l["text"] as String
                                                else
                                                    (l["text"] as Int).toString()
                                                answers.add(
                                                    Answer(
                                                        java.util.UUID.randomUUID().toString().replace("-", ""),
                                                        questionId,
                                                        text,
                                                        isCorrect
                                                    )
                                                )
                                            }
                                            db.quizDao().insertAnswers(*answers.toTypedArray())
                                        }

                                        questions.add(
                                            Question(
                                                questionId,
                                                quizId,
                                                j["text"] as String,
                                                answers,
                                                null
                                            )
                                        )
                                    }
                                    db.quizDao().insertQuestions(*questions.toTypedArray())
                                }

                                //parsing rates
                                val ratesJson = quizItemJsonObject["rates"] as? List<JsonObject>
                                val rates = ArrayList<Rate>()
                                ratesJson?.let {
                                    for (j: JsonObject in ratesJson) {
                                        rates.add(
                                            Rate(
                                                java.util.UUID.randomUUID().toString().replace("-", ""),
                                                quizId,
                                                j["from"] as Int,
                                                j["to"] as Int,
                                                j["content"] as String
                                            )
                                        )
                                    }
                                    db.quizDao().insertRates(*rates.toTypedArray())
                                }

                                quizList.add(
                                    Quiz(
                                        quizId,
                                        i["title"] as String,
                                        i["content"] as String,
                                        (i["mainPhoto"] as JsonObject)["url"] as String,
                                        questions,
                                        rates,
                                        0
                                    )
                                )
                            } catch (e: IOException) {
                                if (e.message != skipQuiz)
                                    throw e
                                QuizApplication.showToast("Error while loading quiz", true)
                            } finally {
                                return@async
                            }
                        })

                    }

                    //waiting for every quiz to load
                    for (i: Deferred<Unit> in awaitItems) {
                        i.await()
                    }
                    db.quizDao().insertQuizzes(*quizList.toTypedArray())
                    loadedCount += 1
                }
            }
        } catch (e: TimeoutCancellationException) {
            QuizApplication.showToast("Connection timeout", true)
        } catch (e: IOException) {
            e.message?.let {
                QuizApplication.showToast(it, true)
            }
        }
        return@withContext quizList
    }
}
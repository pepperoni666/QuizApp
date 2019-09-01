package com.dummy.quiz_app.data

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import com.beust.klaxon.Parser
import com.dummy.quiz_app.app.QuizApplication
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import kotlinx.coroutines.*
import java.io.IOException
import java.lang.StringBuilder

class QuizRepository {

    private val httpClient = OkHttpClient()

    suspend fun getQuizes(): List<Quiz> = withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url("http://quiz.o2.pl/api/v1/quizzes/0/100")
            .build()

        val quizList = ArrayList<Quiz>()

        try {
            //set timeout for API response to 10s
            withTimeout(10000) {
                val response = httpClient.newCall(request).execute()
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val quizListJsonObject: JsonObject =
                    Parser.default().parse(StringBuilder(response.body().string())) as JsonObject

                val items: List<JsonObject>? = quizListJsonObject["items"] as? List<JsonObject>
                items?.let {
                    for(i: JsonObject in items){
                        quizList.add(
                            Quiz(i["id"] as Long, i["title"] as String, i["content"] as String, (i["mainPhoto"] as JsonObject)["url"] as String)
                        )
                    }
                }
            }
        } catch (e: TimeoutCancellationException) {
            QuizApplication.showToast("Connection timeout", true)
        }




        return@withContext quizList
//        return@withContext listOf(
//            Quiz(
//                111,
//                "Quiz1",
//                "This is quiz one.",
//                "https://d.wpimg.pl/32050297--271323561/marsz-rownosci.jpg"
//            ),
//            Quiz(
//                222,
//                "Quiz2",
//                "This is quiz two.",
//                "https://d.wpimg.pl/32050297--271323561/marsz-rownosci.jpg"
//            ),
//            Quiz(
//                222,
//                "Quiz2",
//                "This is quiz three.",
//                "https://d.wpimg.pl/32050297--271323561/marsz-rownosci.jpg"
//            )
//        )
    }
}
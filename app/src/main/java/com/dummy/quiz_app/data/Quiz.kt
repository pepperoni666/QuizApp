package com.dummy.quiz_app.data

import com.beust.klaxon.Json

data class Quiz (
    val id: Long,
    val title: String,
    val info: String,
    val imageUrl: String
)
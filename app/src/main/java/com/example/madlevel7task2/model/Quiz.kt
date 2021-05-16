package com.example.madlevel7task2.model

data class Quiz(
        val question: String = "",
        val answers: ArrayList<String> = arrayListOf(),
        val correctAnswer: Int = 0
)
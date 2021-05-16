package com.example.madlevel7task2.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.madlevel7task2.model.Quiz
import com.example.madlevel7task2.repository.QuizRepository
import kotlinx.coroutines.launch

class QuizViewModel(application: Application) : AndroidViewModel(application) {

    private val quizRepository: QuizRepository = QuizRepository()

    // Retrieve the LiveData with the quizzes and exceptions from the QuizRepository.
    val quizzes: LiveData<ArrayList<Quiz>> = quizRepository.quizzes
    val exception: LiveData<String> = quizRepository.exception

    // Persist the quizzes to Firestore.
    fun createQuizzes() {
        viewModelScope.launch {
            quizRepository.createQuiz(1, Quiz("Who is the co-founder of Android?", arrayListOf("Andy Rubin", "Larry Page & Sergey Brin", "Sundar Pichai"), 1))
            quizRepository.createQuiz(2, Quiz("What was the initial name of Elon Musk's child?", arrayListOf("X Æ A-Xii", "X AE A-XII", "X Æ A-12"), 3))
            quizRepository.createQuiz(3, Quiz("In which year was Apple founded?", arrayListOf("1982", "1976", "1993"), 2))
            quizRepository.createQuiz(4, Quiz("What is Bill Gates' net worth?", arrayListOf("$123.7 billion", "$145.1 billion", "$254.3 billion"), 2))
            quizRepository.createQuiz(5, Quiz("What was Steve Jobs' cause of death?", arrayListOf("Amyotrophic lateral sclerosis (ALS)", "Dementia", "Neuroendocrine cancer"), 3))
        }
    }

    // Retrieve the quizzes from Firestore.
    fun getQuizzes() {
        viewModelScope.launch {
            quizRepository.getQuizzes()
        }
    }
}